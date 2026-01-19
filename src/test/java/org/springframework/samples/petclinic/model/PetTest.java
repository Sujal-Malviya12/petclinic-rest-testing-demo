package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class PetTest {

    @Test
    void shouldAddVisitToPet() {
        Pet pet = new Pet();
        pet.setName("Bruno");

        Visit visit = new Visit();
        visit.setDate(LocalDate.now());
        visit.setDescription("checkup");

        pet.addVisit(visit);

        assertEquals(1, pet.getVisits().size());
        assertEquals(pet, visit.getPet());
        assertEquals("checkup", pet.getVisits().get(0).getDescription());
    }

    @Test
    void setVisitsShouldThrowExceptionWhenNull() {
        Pet pet = new Pet();

        assertThrows(NullPointerException.class, () -> pet.setVisits(null));
    }
}
