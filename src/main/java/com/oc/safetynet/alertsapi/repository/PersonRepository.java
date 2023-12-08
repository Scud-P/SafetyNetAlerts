package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByAddress(String address);

    List<String> findPersonPhoneNumber();
}
