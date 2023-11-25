package com.oc.safetynet.alertsapi.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.service.JsonDataService;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ConsoleView implements CommandLineRunner {

    @Autowired
    PersonController personController;
    @Autowired
    MedicalRecordController medicalRecordController;
    @Autowired
    FireStationController fireStationController;
    @Autowired
    JsonDataService jsonDataService;
    @Autowired
    ObjectMapper objectMapper;


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
        Data data = new Data();
        JSONObject jsonObject = jsonDataService.findDataArrays();

        if (jsonObject != null) {
            try {
                data = objectMapper.readValue(jsonObject.toString(), Data.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            personController.populatePersonsTable();
            System.out.println("Persons table populated!");

            fireStationController.populateFireStationTable();
            System.out.println("FireStations table populated!");

            medicalRecordController.populateMedicalRecordsTable();
            System.out.println("MedicalRecords table populated!");
        }
        System.out.println("...All tables populated!");
    }
}
