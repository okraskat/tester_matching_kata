package io.github.okraskat.tester.matcher.api;

import io.github.okraskat.tester.matcher.TesterMatcher;
import io.github.okraskat.tester.matcher.search.FoundTester;
import io.github.okraskat.tester.matcher.search.SearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
class SearchController {
    private final TesterMatcher testerMatcher;

    @Autowired
    SearchController(TesterMatcher testerMatcher) {
        this.testerMatcher = testerMatcher;
    }

    @GetMapping(value = "/")
    String search(@RequestParam(value = "countries", required = false) String countries,
                  @RequestParam(value = "devices", required = false) String devices,
                  Model model) {
        Set<String> searchCountries = createStringSet(countries);
        Set<String> searchDevices = createStringSet(devices);
        SearchCriteria criteria = new SearchCriteria(searchCountries, searchDevices);
        List<FoundTester> foundTesters = testerMatcher.match(criteria).getTesters();
        model.addAttribute("foundTesters", foundTesters);
        return "index";
    }

    private Set<String> createStringSet(String toSplit) {
        return StringUtils.isNotEmpty(toSplit) ? new HashSet<>(Arrays.asList(toSplit.split(","))) : Collections.emptySet();
    }
}
