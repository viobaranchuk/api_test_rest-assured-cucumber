package com.study.cucumberRestApi;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/api",
        glue = "com/study/cucumberRestApi/stepDefs",
        plugin = {"pretty","html:target/cucumber-report.html"},
        tags = "@AddPet")
public class RunCucumberTest {
}
