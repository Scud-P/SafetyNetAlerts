package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
