package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class NamedEntityTest {

    static class TestNamedEntity extends NamedEntity {
    }

    @Test
    void shouldSetAndGetName() {
        TestNamedEntity e = new TestNamedEntity();
        e.setName("Dog");

        assertEquals("Dog", e.getName());
        assertEquals("Dog", e.toString());
    }
}
