package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void shouldAddPetAndGetPetByName() {
        Owner owner = new Owner();
        owner.setFirstName("Sujal");
        owner.setLastName("Malviya");

        Pet pet = new Pet();
        pet.setName("Tommy");

        PetType type = new PetType();
        type.setName("dog");
        pet.setType(type);

        owner.addPet(pet);

        assertEquals(1, owner.getPets().size());
        assertEquals(owner, pet.getOwner());

        Pet found = owner.getPet("Tommy");
        assertNotNull(found);
        assertEquals("Tommy", found.getName());
    }

    @Test
    void shouldReturnNullWhenPetNotFound() {
        Owner owner = new Owner();
        Pet found = owner.getPet("UnknownPet");
        assertNull(found);
    }

    @Test
    void setPetsShouldThrowExceptionWhenNull() {
        Owner owner = new Owner();

        assertThrows(NullPointerException.class, () -> owner.setPets(null));
    }
}
