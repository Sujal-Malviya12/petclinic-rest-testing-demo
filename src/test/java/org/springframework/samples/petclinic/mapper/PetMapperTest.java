package org.springframework.samples.petclinic.mapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.rest.dto.PetDto;

@SpringBootTest
class PetMapperTest {

    @Autowired
    private PetMapper petMapper;

    @Test
    void shouldMapPetToPetDto() {
        Pet pet = new Pet();
        pet.setId(5);
        pet.setName("Tommy");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));

        PetDto dto = petMapper.toPetDto(pet);

        assertNotNull(dto);
        assertEquals(5, dto.getId());
        assertEquals("Tommy", dto.getName());
    }

    @Test
    void shouldMapPetDtoToPet() {
        PetDto dto = new PetDto();
        dto.setId(6);
        dto.setName("Rocky");

        Pet pet = petMapper.toPet(dto);

        assertNotNull(pet);
        assertEquals(6, pet.getId());
        assertEquals("Rocky", pet.getName());
    }

    @Test
    void shouldMapPetListUsingStream() {
        Pet p1 = new Pet();
        p1.setId(1);
        p1.setName("A");

        Pet p2 = new Pet();
        p2.setId(2);
        p2.setName("B");

        List<PetDto> list = List.of(p1, p2).stream()
                .map(petMapper::toPetDto)
                .toList();

        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(2, list.get(1).getId());
    }
}
