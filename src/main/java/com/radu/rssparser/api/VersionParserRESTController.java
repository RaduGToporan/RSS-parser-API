package com.radu.rssparser.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("versions")
public class VersionParserRESTController {

    @GetMapping("production")
    public String findProductionVersion(@RequestParam("list") String list) {
        //2.5.0-dev.1,2.4.2-5354,2.4.2-test.675,2.4.1-integration.1
        String[] versions = list.split(",");
        Pattern pattern = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+-[0-9]+");
        for (String version : versions) {
            Matcher matcher = pattern.matcher(version);
            if (matcher.matches()) {
                return version.split("-")[0];
            }
        }
        return "No production version found!";
    }
}