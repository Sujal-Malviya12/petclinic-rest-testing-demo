package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PersonTest {

    static class TestPerson extends Person {
    }

    @Test
    void shouldSetAndGetFirstAndLastName() {
        TestPerson p = new TestPerson();
        p.setFirstName("Sujal");
        p.setLastName("Malviya");

        assertEquals("Sujal", p.getFirstName());
        assertEquals("Malviya", p.getLastName());
    }
}
