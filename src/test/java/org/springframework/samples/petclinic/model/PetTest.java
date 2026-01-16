package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class PetTest {

    @Test
    void addVisit_shouldAddVisitAndSetPet() {
        Pet pet = new Pet();
        pet.setName("Tommy");

        Visit visit = new Visit();
        visit.setDate(LocalDate.now());
        visit.setDescription("Checkup");

        pet.addVisit(visit);

        assertNotNull(pet.getVisits());
        assertEquals(1, pet.getVisits().size());
        assertEquals(pet, visit.getPet());
    }

    @Test
    void toString_shouldNotThrow() {
        Pet pet = new Pet();
        pet.setName("TestPet");
        assertDoesNotThrow(pet::toString);
    }
}
