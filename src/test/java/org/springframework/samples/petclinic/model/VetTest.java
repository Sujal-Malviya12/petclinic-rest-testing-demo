package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class VetTest {

    @Test
    void shouldAddSpecialtyToVet() {
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Doe");

        Specialty surgery = new Specialty();
        surgery.setId(1);
        surgery.setName("surgery");

        // add specialty
        vet.addSpecialty(surgery);

        // verify
        assertNotNull(vet.getSpecialties());
        assertEquals(1, vet.getSpecialties().size());

        Specialty added = vet.getSpecialties().iterator().next();
        assertEquals("surgery", added.getName());
        assertEquals(1, added.getId());
    }

    @Test
    void shouldAddMultipleSpecialties() {
        Vet vet = new Vet();

        Specialty s1 = new Specialty();
        s1.setId(1);
        s1.setName("dentistry");

        Specialty s2 = new Specialty();
        s2.setId(2);
        s2.setName("radiology");

        vet.addSpecialty(s1);
        vet.addSpecialty(s2);

        assertEquals(2, vet.getSpecialties().size());
    }
}
