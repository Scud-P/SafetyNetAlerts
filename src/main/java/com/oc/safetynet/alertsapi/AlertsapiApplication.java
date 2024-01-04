package com.oc.safetynet.alertsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.oc.safetynet.alertsapi")
public class AlertsapiApplication implements CommandLineRunner {

	@Autowired
	private PersonController personController;

	@Autowired
	private MedicalRecordController medicalRecordController;

	@Autowired
	private FireStationController fireStationController;

	private static final Logger logger = LoggerFactory.getLogger(AlertsapiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AlertsapiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		populateDB();
	}

	private void populateDB() {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			InputStream inputStream = getClass().getResourceAsStream("/data.json");
			Data data = objectMapper.readValue(inputStream, Data.class);
			logger.debug("Content of JSON source files: {}", data);

			List<Person> persons = data.getPersons();
			personController.addAllPersons(persons);
			logger.debug("Persons: {} ", persons);

			List<MedicalRecord> medicalrecords = data.getMedicalrecords();
			medicalRecordController.addAllMedicalRecords(medicalrecords);
			logger.debug("Medical Records: {} ", medicalrecords);

			List<FireStation> firestations = data.getFirestations();
			fireStationController.addAllFireStations(firestations);
			logger.debug("Firestations: {} ", firestations);

		} catch (IOException e) {
			logger.error("Error reading data from JSON file" + e.getMessage());
		}
	}

	//TODO Surefire reports
	//TODO Successful Queries -> Log Info
	//TODO Exceptions and Errors -> Log Error
	//TODO Steps and Logic -> Log Debug
}
