package ke.co.ngonyoku.covid_track.services;

import ke.co.ngonyoku.covid_track.models.CasesConfirmedGlobally;
import ke.co.ngonyoku.covid_track.models.RecoveriesConfirmedGlobally;
import ke.co.ngonyoku.covid_track.models.DeathsConfirmedGlobally;
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
    public static final String CONFIRMED_DEATHS_GLOBAL_URL = "https://raw.githubusercontent.com/Ngonyoku/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    public static final String CONFIRMED_RECOVERED_CASES_GLOBAL = "https://raw.githubusercontent.com/Ngonyoku/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    private List<CasesConfirmedGlobally> casesConfirmedGloballyList = new ArrayList<>();
    private List<DeathsConfirmedGlobally> deathsConfirmedGloballyList = new ArrayList<>();
    private List<RecoveriesConfirmedGlobally> recoveriesConfirmedGloballyList = new ArrayList<>();

    private Iterable<CSVRecord> fetchRecordFromCSV(String URL) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient(); //Create a HttpClient
        URI generatedURI = URI.create(URL);//Extract the URI from our CSV Url
        HttpRequest request
                = HttpRequest
                .newBuilder()
                .uri(generatedURI)
                .build();

        /*Get the Response*/
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        /*Get the Response Response Body*/
        String responseBody = response.body();
        StringReader stringReader = new StringReader(responseBody);

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);

        return records;
    }

    @PostConstruct //This method will be called when the application boots
    @Scheduled(cron = "* 0 * * * *") // The method will be called every 5 seconds according to the cron expression
    public void fetchData() throws IOException, InterruptedException {
        List<CasesConfirmedGlobally> newCasesConfirmedGloballyList = new ArrayList<>();
        List<DeathsConfirmedGlobally> newDeathConfirmedGlobally = new ArrayList<>();
        List<RecoveriesConfirmedGlobally> newRecoveriesConfirmedGloballyList = new ArrayList<>();

        /*Loop through the Confirmed Cases Records*/
        for (CSVRecord record : fetchRecordFromCSV(CONFIRMED_CASES_GLOBAL_URL)) {
            /*The last Column contains the latest covid cases*/
            int lastColumn = Integer.parseInt(record.get(record.size() - 1));
            /*Create Instances*/
            CasesConfirmedGlobally casesConfirmedGlobally = new CasesConfirmedGlobally(
                    record.get("Country/Region"), //CountryOrRegion
                    record.get("Province/State"), //ProvinceOrState
                    record.get("Lat"), //Latitude
                    record.get("Long"), //Longitude
                    lastColumn //Latest Cases Reported
            );

            newCasesConfirmedGloballyList.add(casesConfirmedGlobally); //Populate our List with the data
        }

        /*Loop through the Confirmed Deaths Records*/
        for (CSVRecord record : fetchRecordFromCSV(CONFIRMED_DEATHS_GLOBAL_URL)) {
            int lastColumn = Integer.parseInt(record.get(record.size() - 1));//Get recent updates in the last column
            /*Create the Instances*/
            DeathsConfirmedGlobally deathsConfirmedGlobally =
                    new DeathsConfirmedGlobally(
                            record.get("Country/Region"), //CountryOrRegion
                            record.get("Province/State"), //ProvinceOrState
                            record.get("Lat"), //Latitude
                            record.get("Long"), //Longitude
                            lastColumn //Latest Cases Reported
                    );

            newDeathConfirmedGlobally.add(deathsConfirmedGlobally);//Populate our List
        }

        /*Loop through the Confirmed Deaths Records*/
        for (CSVRecord record : fetchRecordFromCSV(CONFIRMED_RECOVERED_CASES_GLOBAL)) {
            int lastColumn = Integer.parseInt(record.get(record.size() - 1));//Get recent updates in the last column
            RecoveriesConfirmedGlobally recoveriesConfirmedGlobally =
                    new RecoveriesConfirmedGlobally(
                            record.get("Country/Region"), //CountryOrRegion
                            record.get("Province/State"), //ProvinceOrState
                            record.get("Lat"), //Latitude
                            record.get("Long"), //Longitude
                            lastColumn //Latest Cases Reported
                    );
            newRecoveriesConfirmedGloballyList.add(recoveriesConfirmedGlobally);
        }

        /*Update the current list of cases*/
        this.casesConfirmedGloballyList = newCasesConfirmedGloballyList;
        this.deathsConfirmedGloballyList = newDeathConfirmedGlobally;
        this.recoveriesConfirmedGloballyList = newRecoveriesConfirmedGloballyList;
    }
}
