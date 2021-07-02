package ke.co.ngonyoku.covid_track.controllers;

import ke.co.ngonyoku.covid_track.models.CasesConfirmedGlobally;
import ke.co.ngonyoku.covid_track.models.DeathsConfirmedGlobally;
import ke.co.ngonyoku.covid_track.services.CovidTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private CovidTrackerService covidTrackerService;

    @GetMapping("/")
    public String getHome(Model model) {
        List<CasesConfirmedGlobally> casesConfirmedGloballyList = covidTrackerService.getCasesConfirmedGloballyList();
        List<DeathsConfirmedGlobally> deathsConfirmedGloballyList = covidTrackerService.getDeathsConfirmedGloballyList();

        int totalCasesReported = casesConfirmedGloballyList
                .stream()
                .mapToInt(
                        casesList -> casesList.getLatestTotalCasesReported()
                )
                .sum(); //Returns the Sum of all the Cases reported
        int totalDeathsReported = deathsConfirmedGloballyList
                .stream()
                .mapToInt(
                        deathsList -> deathsList.getTotalDeathsConfirmed()
                )
                .sum(); //Returns the Sum of all the Deaths reported

        model.addAttribute("confirmedCasesList", casesConfirmedGloballyList);
        model.addAttribute("confirmedDeathsList", deathsConfirmedGloballyList);
        model.addAttribute("totalCasesReported", totalCasesReported);
        model.addAttribute("totalDeathsReported", totalDeathsReported);
        return "index";
    }
}
