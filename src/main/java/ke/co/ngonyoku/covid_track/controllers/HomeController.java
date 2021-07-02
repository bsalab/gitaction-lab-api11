package ke.co.ngonyoku.covid_track.controllers;

import ke.co.ngonyoku.covid_track.models.CasesConfirmedGlobally;
import ke.co.ngonyoku.covid_track.models.DeathsConfirmedGlobally;
import ke.co.ngonyoku.covid_track.models.RecoveriesConfirmedGlobally;
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

    public static final String APP_NAME = "Covid-Track";

    @GetMapping("/")
    public String home(Model model) {
        List<CasesConfirmedGlobally> casesConfirmedGloballyList =
                covidTrackerService.getCasesConfirmedGloballyList();
        List<DeathsConfirmedGlobally> deathsConfirmedGloballyList =
                covidTrackerService.getDeathsConfirmedGloballyList();
        List<RecoveriesConfirmedGlobally> recoveriesConfirmedGloballyList =
                covidTrackerService.getRecoveriesConfirmedGloballyList();
        int totalCasesReported = casesConfirmedGloballyList
                .stream()
                .mapToInt(
                        casesList -> casesList.getLatestTotalCasesReported()
                )
                .sum(); //Returns the Sum of all the Cases reported
        int totalDeathsReported = deathsConfirmedGloballyList
                .stream()
                .mapToInt(
                        deathsList -> deathsList.getLatestTotalDeathsConfirmed()
                )
                .sum(); //Returns the Sum of all the Deaths reported
        int totalRecoveriesReported = recoveriesConfirmedGloballyList
                .stream()
                .mapToInt(
                        recoveryList -> recoveryList.getLatestTotalRecoveriesReported()
                )
                .sum();

        model.addAttribute("appName", APP_NAME);

        model.addAttribute("casesConfirmedGloballyList", casesConfirmedGloballyList);
        model.addAttribute("deathsConfirmedGloballyList", deathsConfirmedGloballyList);
        model.addAttribute("recoveriesConfirmedGloballyList", recoveriesConfirmedGloballyList);

        model.addAttribute("totalCasesReported", totalCasesReported);
        model.addAttribute("totalRecoveriesReported", totalRecoveriesReported);
        model.addAttribute("totalDeathsReported", totalDeathsReported);
        return "index";
    }
}
