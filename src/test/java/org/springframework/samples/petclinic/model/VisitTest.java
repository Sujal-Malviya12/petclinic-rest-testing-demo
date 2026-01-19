package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class VisitTest {

    @Test
    void shouldSetVisitFields() {
        Visit visit = new Visit();
        visit.setId(10);
        visit.setDate(LocalDate.of(2026, 1, 19));
        visit.setDescription("rabies vaccine");

        assertEquals(10, visit.getId());
        assertEquals(LocalDate.of(2026, 1, 19), visit.getDate());
        assertEquals("rabies vaccine", visit.getDescription());
    }
}
