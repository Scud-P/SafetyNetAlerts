package com.oc.safetynet.alertsapi.controller;

import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.repository.PersonRepoImpl;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepoImpl personRepoImpl;

    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person) {
        return personRepoImpl.addPersonToList(person);
    }

    @DeleteMapping("/person")
    public void deletePerson(@RequestBody Map<String, String> requestBody) {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        personRepoImpl.deletePersonFromList(firstName, lastName);
    }

    @PutMapping("/person")
    public Person updatePerson (@RequestBody Person person) {
        return personRepoImpl.updatePerson(person);
    }


    @GetMapping(value = "/flood/stations", params = "stationNumber")
    public List<HomeDTO> getAddressesByStation(@RequestParam(name = "stationNumber") int station) {
        return personService.findHomesByAddresses(station);
    }

//    @GetMapping(value = "/firestation", params = "stationNumber")
//    public PersonWithCountDTO getPersonsByStation(@RequestParam(name = "stationNumber") int station) {
//        return personService.findPersonsByStation(station);
//    }

//    @GetMapping(value = "/childAlert", params = "address")
//    public List<ChildDTO> getAllMinorsAtAddress(@RequestParam(name = "address") String address) {
//        return personService.findMinorsAtAddress(address);
//    }

//    @GetMapping(value = "/fire", params = "address")
//    public PersonFireWithStationNumberDTO getAllPersonsAtAddress(@RequestParam(name = "address") String address) {
//        return personService.findPersonsAtAddress(address);
//    }
//
//    @PostMapping("/batchperson")
//    public List<Person> addAllPersons(@RequestBody List<Person> persons) {
//        return personService.saveAllPersons(persons);
//    }
//    @GetMapping(value = "/phoneAlert", params = "firestation")
//    public List<String> getPhonesByStation(@RequestParam(name = "firestation") int station) {
//        return personService.findPhonesByStation(station);
//    }

    @GetMapping(value = "/communityEmail", params = "city")
    public List<String> getEmailsByCity(@RequestParam(name = "city") String city) {
        return personRepoImpl.findEmailsByCity(city);
    }

    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfoListDTO(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName) {

        List<PersonInfoDTO> personInfoList = personService.findPersonInfoListDTO(firstName, lastName);

        if (personInfoList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(personInfoList, HttpStatus.OK);
        }
    }
}
