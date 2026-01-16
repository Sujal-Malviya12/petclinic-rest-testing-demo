package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class VetTest {

    @Test
    void addSpecialty_shouldAddSpecialty() {
        Vet vet = new Vet();
        vet.setFirstName("John");

        Specialty specialty = new Specialty();
        specialty.setName("surgery");

        vet.addSpecialty(specialty);

        assertNotNull(vet.getSpecialties());
        assertEquals(1, vet.getSpecialties().size());
        assertTrue(vet.getSpecialties().contains(specialty));
    }
}
