package com.szelestamas.bookstorebddtest;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest(classes = BookstoreBddTestConfig.class)
@ComponentScan
@CucumberContextConfiguration
public class BookstoreBddTestConfig {
}
