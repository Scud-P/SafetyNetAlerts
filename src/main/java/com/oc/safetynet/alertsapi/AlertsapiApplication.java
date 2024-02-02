package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "com.oc.safetynet.alertsapi")
@PropertySource("classpath:application.properties")

public class AlertsapiApplication implements CommandLineRunner {

	@Autowired
	private DataRepository dataRepository;

	public static void main(String[] args) {
		SpringApplication.run(AlertsapiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		dataRepository.readData();
	}
}
