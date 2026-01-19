package org.springframework.samples.petclinic.mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;

@SpringBootTest
class PetMapperTest {

    @Autowired
    private PetMapper petMapper;

    @Test
    void shouldMapPetToPetDto_withOwnerId() {
        Owner owner = new Owner();
        owner.setId(50);

        Pet pet = new Pet();
        pet.setId(10);
        pet.setName("Tommy");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));
        pet.setOwner(owner);

        PetDto dto = petMapper.toPetDto(pet);

        assertNotNull(dto);
        assertEquals(10, dto.getId());
        assertEquals("Tommy", dto.getName());
        assertEquals(50, dto.getOwnerId());
    }

    @Test
    void shouldMapPetDtoToPet_withOwnerIdToOwner() {
        PetDto dto = new PetDto();
        dto.setId(11);
        dto.setName("Rocky");
        dto.setOwnerId(77);

        Pet pet = petMapper.toPet(dto);

        assertNotNull(pet);
        assertEquals(11, pet.getId());
        assertEquals("Rocky", pet.getName());
        assertNotNull(pet.getOwner());
        assertEquals(77, pet.getOwner().getId());
    }

    @Test
    void shouldMapPetFieldsDtoToPet_andIgnoreIdOwnerVisits() {
        PetFieldsDto fieldsDto = new PetFieldsDto();
        fieldsDto.setName("Kitty");
        fieldsDto.setBirthDate(LocalDate.of(2022, 2, 2));

        Pet pet = petMapper.toPet(fieldsDto);

        assertNotNull(pet);
        assertEquals("Kitty", pet.getName());

        // ignored fields
        assertNull(pet.getId());
        assertNull(pet.getOwner());
        assertTrue(pet.getVisits() == null || pet.getVisits().isEmpty());
    }

    @Test
    void shouldMapPetsCollectionToPetDtoCollection() {
        Pet p1 = new Pet();
        p1.setId(1);
        p1.setName("A");

        Pet p2 = new Pet();
        p2.setId(2);
        p2.setName("B");

        Collection<PetDto> dtoList = petMapper.toPetsDto(List.of(p1, p2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
    }

    @Test
    void shouldMapPetDtoCollectionToPetCollection() {
        PetDto d1 = new PetDto();
        d1.setId(1);
        d1.setName("X");

        PetDto d2 = new PetDto();
        d2.setId(2);
        d2.setName("Y");

        Collection<Pet> pets = petMapper.toPets(List.of(d1, d2));

        assertNotNull(pets);
        assertEquals(2, pets.size());
    }

    @Test
    void shouldMapPetTypeToDtoAndBack() {
        PetType type = new PetType();
        type.setId(5);
        type.setName("dog");

        PetTypeDto dto = petMapper.toPetTypeDto(type);
        assertNotNull(dto);
        assertEquals(5, dto.getId());
        assertEquals("dog", dto.getName());

        PetType back = petMapper.toPetType(dto);
        assertNotNull(back);
        assertEquals(5, back.getId());
        assertEquals("dog", back.getName());
    }

    @Test
    void shouldMapPetTypeCollectionToDtoCollection() {
        PetType t1 = new PetType();
        t1.setId(1);
        t1.setName("dog");

        PetType t2 = new PetType();
        t2.setId(2);
        t2.setName("cat");

        Collection<PetTypeDto> result = petMapper.toPetTypeDtos(List.of(t1, t2));

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
