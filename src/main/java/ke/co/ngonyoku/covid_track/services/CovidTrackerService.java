package ke.co.ngonyoku.covid_track.services;

import ke.co.ngonyoku.covid_track.models.ConfirmedCasesGlobal;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class CovidTrackerService {
    public static final String CONFIRMED_CASES_GLOBAL_URL = "https://raw.githubusercontent.com/Ngonyoku/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<ConfirmedCasesGlobal> confirmedCasesGlobalList = new ArrayList<>();

    @PostConstruct //This method will be called when the application boots
    @Scheduled(cron = "* 0 * * * *") // The method will be called every 5 seconds according to the cron expression
    public void fetchData() throws IOException, InterruptedException {
        List<ConfirmedCasesGlobal> newConfirmedCasesGlobalList = new ArrayList<>();

        HttpClient httpClient = HttpClient.newHttpClient();
        URI confirmedCaseURI = URI.create(CONFIRMED_CASES_GLOBAL_URL); // Get the URI from the URL
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(confirmedCaseURI)
                .build();

        /*Get the Response*/
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        /*Covert the body to StringReader*/
        StringReader confirmedCasesBodyReader = new StringReader(response.body());

        Iterable<CSVRecord> confirmedCasesRecord =
                CSVFormat
                        .DEFAULT
                        .withFirstRecordAsHeader()
                        .parse(confirmedCasesBodyReader);
        /*Loop through the Records*/
        for (CSVRecord record : confirmedCasesRecord) {
            /*The last Column contains the latest covid cases*/
            int lastColumn = Integer.parseInt(record.get(record.size() - 1));

            /*Create Instances*/
            ConfirmedCasesGlobal confirmedCasesGlobal = new ConfirmedCasesGlobal();
            confirmedCasesGlobal.setProvinceOrState(record.get("Province/State"));
            confirmedCasesGlobal.setCountryOrRegion(record.get("Country/Region"));
            confirmedCasesGlobal.setLatitude(record.get("Lat"));
            confirmedCasesGlobal.setLatitude(record.get("Long"));
            confirmedCasesGlobal.setLatestTotalCasesReported(lastColumn);

            newConfirmedCasesGlobalList.add(confirmedCasesGlobal); //Populate our List with the data
        }

        this.confirmedCasesGlobalList = newConfirmedCasesGlobalList; //Update the current list of cases
    }
}
