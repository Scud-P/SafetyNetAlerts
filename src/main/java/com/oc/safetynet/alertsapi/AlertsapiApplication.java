package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@SpringBootApplication(scanBasePackages = "com.oc.safetynet.alertsapi")
@PropertySource("classpath:application.properties")

public class AlertsapiApplication implements CommandLineRunner {

	private PersonController personController;

	private MedicalRecordController medicalRecordController;

	private FireStationController fireStationController;

	@Autowired
	private DataRepository dataRepository;

	private static final Logger logger = LoggerFactory.getLogger(AlertsapiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AlertsapiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Data data = dataRepository.readData();
		List<Person> persons = data.getPersons();
		logger.info("Persons: {}", persons);
		List<MedicalRecord> medicalRecords = data.getMedicalrecords();
		logger.info("Medical Records: {}", medicalRecords);
		List<FireStation> fireStations = data.getFirestations();
		logger.info("Firestations: {}", fireStations);
	}
}
