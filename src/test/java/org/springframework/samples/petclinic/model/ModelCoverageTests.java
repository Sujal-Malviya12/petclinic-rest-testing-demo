package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelCoverageTests {

    @Test
    void ownerAddPet_shouldWork() {
        Owner owner = new Owner();
        owner.setFirstName("Sujal");

        Pet pet = new Pet();
        pet.setName("Rex");

        owner.addPet(pet);

        assertEquals(1, owner.getPets().size());
        assertEquals(owner, pet.getOwner());
    }

    @Test
    void vetAddSpecialty_shouldWork() {
        Vet vet = new Vet();
        Specialty sp = new Specialty();
        sp.setName("surgery");

        vet.addSpecialty(sp);

        assertTrue(vet.getSpecialties().contains(sp));
    }
}
