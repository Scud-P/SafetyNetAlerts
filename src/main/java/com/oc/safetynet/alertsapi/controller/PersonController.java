package com.oc.safetynet.alertsapi.controller;

import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.ChildDTO;
import com.oc.safetynet.alertsapi.model.dto.PersonDTO;
import com.oc.safetynet.alertsapi.model.dto.PersonWithCountDTO;
import com.oc.safetynet.alertsapi.service.FireStationService;
import com.oc.safetynet.alertsapi.service.PersonService;
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
    public PersonWithCountDTO getPersonsByStation(@RequestParam(name = "stationNumber") int station) {
        return personService.findPersonsByStation(station);
    }

    @GetMapping(value = "/childAlert", params = "address")
    public List<ChildDTO> getAllMinorsAtAddress(@RequestParam(name = "address") String address) {
        return personService.findMinorsAtAddress(address);
    }


    @PostMapping
    public Person addPerson(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    @PostMapping("/batch")
    public List<Person> addAllPersons(@RequestBody List<Person> persons) {
        return personService.saveAllPersons(persons);
    }
    @GetMapping(value = "/phoneAlert", params = "firestation")
    public List<String> getPhonesByStation(@RequestParam(name = "firestation") int station) {
        return personService.findPhonesByStation(station);
    }

}
