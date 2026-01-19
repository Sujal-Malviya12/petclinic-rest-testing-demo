package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PetTypeTest {

    @Test
    void shouldSetAndGetPetTypeName() {
        PetType type = new PetType();
        type.setName("cat");

        assertEquals("cat", type.getName());
    }
}
