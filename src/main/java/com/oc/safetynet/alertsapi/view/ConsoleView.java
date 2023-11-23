package com.oc.safetynet.alertsapi.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ConsoleView implements CommandLineRunner {

    @Autowired
    PersonController personController;


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
            List<Person> persons = data.getPersons();
            personController.addAllPersons(persons);
            System.out.println("DB populated!");
        } catch(IOException e) {
            System.out.println("Error reading data from JSON file" + e.getMessage());
        }
    }
}
