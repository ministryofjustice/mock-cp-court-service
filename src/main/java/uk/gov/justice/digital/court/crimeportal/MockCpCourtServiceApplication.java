package uk.gov.justice.digital.court.crimeportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class MockCpCourtServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockCpCourtServiceApplication.class, args);
    }

}
