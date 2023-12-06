package com.oc.safetynet.alertsapi.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;



@Component
@Slf4j
public class ConsoleView implements CommandLineRunner {

    @Autowired
    PersonController personController;
    @Autowired
    MedicalRecordController medicalRecordController;
    @Autowired
    FireStationController fireStationController;
    private static final Logger logger = LoggerFactory.getLogger(ConsoleView.class);


    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Populate DB");
            System.out.println("2. Exit Program");
            System.out.println("Chose an option");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    populateDB();
                    break;
                case 2:
                    System.out.println("Exiting program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice, please try again...");
            }
        }
    }

    private void populateDB() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            InputStream inputStream = getClass().getResourceAsStream("/data.json");
            Data data = objectMapper.readValue(inputStream, Data.class);

            logger.info("Content of JSON source files: {}", data);

            List<Person> persons = data.getPersons();
            personController.addAllPersons(persons);
            logger.info("Persons: {} ", persons);
            System.out.println("Persons DB populated");

            List<MedicalRecord> medicalrecords = data.getMedicalrecords();
            medicalRecordController.addAllMedicalRecords(medicalrecords);
            logger.info("Medical Records: {} ", medicalrecords);
            System.out.println("Medical Records DB populated");

            List<FireStation> firestations = data.getFirestations();
            fireStationController.addAllFireStations(firestations);
            logger.info("Firestations: {} ", firestations);
            System.out.println("Firestations DB populated");


        } catch (IOException e) {
            System.out.println("Error reading data from JSON file" + e.getMessage());
        }
    }
}
