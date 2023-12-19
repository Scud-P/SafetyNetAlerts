package com.oc.safetynet.alertsapi.controller;

import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.service.FireStationService;
import com.oc.safetynet.alertsapi.service.PersonService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/person")
    public Person getPerson(@RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName) {
        return personService.getPersonByFirstNameAndLastName(firstName, lastName);
    }

    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

    @DeleteMapping("/person")
    public void deletePerson(@RequestBody Person person) {
        personService.deletePerson(person);
    }

    @PutMapping("/person")
    public Person updatePerson (@RequestBody Person person) {
        return personService.updatePerson(person);
    }


    @GetMapping(value = "/flood/stations", params = "stationNumber")
    public List<HomeDTO> getAddressesByStation(@RequestParam(name = "stationNumber") int station) {
        return personService.findHomesByStation(station);
    }

    @GetMapping(value = "/firestation", params = "stationNumber")
    public PersonWithCountDTO getPersonsByStation(@RequestParam(name = "stationNumber") int station) {
        return personService.findPersonsByStation(station);
    }

    @GetMapping(value = "/childAlert", params = "address")
    public List<ChildDTO> getAllMinorsAtAddress(@RequestParam(name = "address") String address) {
        return personService.findMinorsAtAddress(address);
    }

    @GetMapping(value = "/fire", params = "address")
    public PersonFireWithStationNumberDTO getAllPersonsAtAddress(@RequestParam(name = "address") String address) {
        return personService.findPersonsAtAddress(address);
    }

    @PostMapping("/batchperson")
    public List<Person> addAllPersons(@RequestBody List<Person> persons) {
        return personService.saveAllPersons(persons);
    }
    @GetMapping(value = "/phoneAlert", params = "firestation")
    public List<String> getPhonesByStation(@RequestParam(name = "firestation") int station) {
        return personService.findPhonesByStation(station);
    }

    @GetMapping(value = "/communityEmail", params = "city")
    public List<String> getEmailsByCity(@RequestParam(name = "city") String city) {
        return personService.findEmailsByCity(city);
    }

    @GetMapping(value = "/personInfo", params = {"firstName", "lastName"})
    public List<PersonInfoDTO> getPersonByFirstNameAndLastName(@RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName) {
        return personService.findPersonInfoListDTO(firstName, lastName);
    }
}
