package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SpecialtyTest {

    @Test
    void shouldSetAndGetName() {
        Specialty s = new Specialty();
        s.setName("dentistry");

        assertEquals("dentistry", s.getName());
    }
}
