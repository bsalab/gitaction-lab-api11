package ke.co.ngonyoku.covid_track.controllers;

import ke.co.ngonyoku.covid_track.models.ConfirmedCasesGlobal;
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
        List<ConfirmedCasesGlobal> confirmedCasesGlobalList = covidTrackerService.getConfirmedCasesGlobalList();
        int totalCasesReported = confirmedCasesGlobalList
                .stream()
                .mapToInt(
                        casesList -> casesList.getLatestTotalCasesReported()
                )
                .sum(); //Returns the Sum of all the Cases reported
        model.addAttribute("confirmedCasesList", confirmedCasesGlobalList);
        model.addAttribute("totalCasesReported", totalCasesReported);
        return "index";
    }
}
