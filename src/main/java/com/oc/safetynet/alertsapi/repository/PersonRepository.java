package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByAddress(String address);

    @Query("SELECT p.phone FROM Person p WHERE p.address = :address")
    List<String> findPhoneByAddress(@Param("address") String address);

}
