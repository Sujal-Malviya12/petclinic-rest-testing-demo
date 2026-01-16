package org.springframework.samples.petclinic.mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;

@SpringBootTest
class PetTypeMapperTest {

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Test
    void shouldMapPetTypeToDto() {
        PetType type = new PetType();
        type.setId(1);
        type.setName("dog");

        PetTypeDto dto = petTypeMapper.toPetTypeDto(type);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("dog", dto.getName());
    }

    @Test
    void shouldMapDtoToPetType() {
        PetTypeDto dto = new PetTypeDto();
        dto.setId(2);
        dto.setName("cat");

        PetType type = petTypeMapper.toPetType(dto);

        assertNotNull(type);
        assertEquals(2, type.getId());
        assertEquals("cat", type.getName());
    }

    @Test
    void shouldMapPetTypeList() {
        PetType t1 = new PetType(); t1.setId(1); t1.setName("dog");
        PetType t2 = new PetType(); t2.setId(2); t2.setName("cat");

        List<PetTypeDto> list = petTypeMapper.toPetTypeDtos(List.of(t1, t2));

        assertEquals(2, list.size());
    }
}
