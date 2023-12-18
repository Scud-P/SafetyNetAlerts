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

    Person findByFirstNameAndLastName(String firstName, String lastName);

    void deleteByFirstNameAndLastName(String firstName, String lastName);


    @Query("SELECT DISTINCT p.phone FROM Person p WHERE p.address = :address")
    List<String> findPhoneByAddress(@Param("address") String address);

    @Query("SELECT DISTINCT p.phone FROM Person p WHERE p.address in :addresses")
    List<String> findPhoneByAddresses(@Param("addresses") List<String> addresses);

    @Query("SELECT p.email FROM Person p WHERE LOWER(p.city) = LOWER(:city)")
    List<String> findEmailsByCity(@Param("city") String city);

    @Query("SELECT p FROM Person p WHERE LOWER(p.firstName) = LOWER(:firstName) AND LOWER(p.lastName) = LOWER(:lastName)")
    List<Person> findPersonByFirstNameAndLastName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
        );

    List<Person> findAllByAddress(@Param("address") String address);

    @Query("SELECT p FROM Person p WHERE p.address IN :addresses")
    List<Person> findByAddresses(@Param("addresses") List<String> addresses);
}