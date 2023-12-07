package com.oc.safetynet.alertsapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.PersonsWithMinorCount;
import com.oc.safetynet.alertsapi.service.FireStationService;
import com.oc.safetynet.alertsapi.service.PersonService;
import com.oc.safetynet.alertsapi.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping(value = "/firestation", params = "stationNumber")
    @JsonView(Views.ChildAlert.class)
    public PersonsWithMinorCount getPersonsByStation(@RequestParam(name = "stationNumber") int station) {
        return personService.getPersonsByStationWithMinorsAndCount(station);
    }

    @GetMapping(value = "/childAlert", params = "address")
    @JsonView(Views.ChildAlert.class)
    public List<Person> getAllMinorsAtAddress(@RequestParam(name = "address") String address) {
        return personService.getMinorsByAddress(address);
    }


    @PostMapping
    public Person addPerson(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    @PostMapping("/batch")
    public List<Person> addAllPersons(@RequestBody List<Person> persons) {
        return personService.saveAllPersons(persons);
    }
}
