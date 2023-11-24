package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.repository.PersonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private JsonDataService jsonDataService;

    public void populatePersonTable() {
        JSONObject data = jsonDataService.findDataArrays();
        if(data !=null) {
            JSONArray personsArray = data.getJSONArray("persons");
            List<Person> persons = new ArrayList<>();
            for (int i = 0; i < personsArray.length(); i++) {
                JSONObject personJson = personsArray.getJSONObject(i);
                Person person = new Person();
                person.setFirstName(personJson.getString("firstName"));
                person.setLastName(personJson.getString("lastName"));
                person.setAddress(personJson.getString("address"));
                person.setCity(personJson.getString("city"));
                person.setZip(personJson.getString("zip"));
                person.setPhone(personJson.getString("phone"));
                person.setEmail(personJson.getString("email"));
                persons.add(person);
            }
            personRepository.saveAll(persons);
        }
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> saveAllPersons(List<Person> persons) {
        return personRepository.saveAll(persons);
    }
}
