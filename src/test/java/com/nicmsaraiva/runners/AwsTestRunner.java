package com.nicmsaraiva.runners;

import io.cucumber.junit.CucumberOptions;

@CucumberOptions(
        glue = {"features/aws"}
)
public class AwsTestRunner {
}
