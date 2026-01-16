package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void addPet_shouldSetOwnerAndAddPet() {
        Owner owner = new Owner();
        owner.setFirstName("Sujal");

        Pet pet = new Pet();
        pet.setName("Tommy");

        owner.addPet(pet);

        assertNotNull(owner.getPets());
        assertEquals(1, owner.getPets().size());
        assertEquals(owner, pet.getOwner());
    }

    @Test
    void getPet_shouldReturnPetByName_caseInsensitive() {
        Owner owner = new Owner();

        Pet pet = new Pet();
        pet.setName("Rocky");
        owner.addPet(pet);

        Pet found = owner.getPet("rocky");
        assertNotNull(found);
        assertEquals("Rocky", found.getName());
    }

    @Test
    void getPet_shouldReturnNullWhenNotFound() {
        Owner owner = new Owner();

        Pet found = owner.getPet("unknown");
        assertNull(found);
    }
}
