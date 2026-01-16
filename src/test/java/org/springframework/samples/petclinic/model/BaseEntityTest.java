package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class BaseEntityTest {

    @Test
    void shouldSetAndGetId() {
        Owner owner = new Owner();
        owner.setId(99);
        assertEquals(99, owner.getId());
    }

    @Test
    void isNew_shouldReturnTrueIfIdNull() {
        Owner owner = new Owner();
        owner.setId(null);
        assertTrue(owner.isNew());
    }

    @Test
    void isNew_shouldReturnFalseIfIdNotNull() {
        Owner owner = new Owner();
        owner.setId(1);
        assertFalse(owner.isNew());
    }
}
