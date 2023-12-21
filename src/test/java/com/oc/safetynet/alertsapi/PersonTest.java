package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.Person;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PersonTest {

    @Test
    public void testEquals() {
        Person person = new Person(1L,"Edouard", "Balladur", "test address", "test city", "test zip", "test phone", "test email");
        assertEquals(person, person);

        Person person1 = new Person(1L, "John", "Doe", "address", "city", "zip", "phone", "email");
        Person person2 = new Person(1L, "John", "Doe", "address", "city", "zip", "phone", "email");
        assertEquals(person1, person2);
        assertEquals(person2, person1);
    }

    @Test
    public void testHashCode() {
        Person person = new Person(1L, "John", "Doe", "address", "city", "zip", "phone", "email");
        int hashCode1 = person.hashCode();
        int hashCode2 = person.hashCode();
        assertEquals(hashCode1, hashCode2);

        Person person1 = new Person(1L, "John", "Doe", "address", "city", "zip", "phone", "email");
        Person person2 = new Person(1L, "John", "Doe", "address", "city", "zip", "phone", "email");
        assertEquals(person1, person2);
        assertEquals(person1.hashCode(), person2.hashCode());

        Person differentPerson = new Person(2L, "Jane", "Doe", "address", "city", "zip", "phone", "email");
        assertNotEquals(person.hashCode(), differentPerson.hashCode());

    }

}
