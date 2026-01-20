package org.springframework.samples.petclinic.mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeFieldsDto;

@SpringBootTest
class PetTypeMapperTest {

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Test
    void shouldMapPetTypeToDtoAndBack() {
        PetType type = new PetType();
        type.setId(1);
        type.setName("dog");

        PetTypeDto dto = petTypeMapper.toPetTypeDto(type);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("dog", dto.getName());

        PetType back = petTypeMapper.toPetType(dto);

        assertNotNull(back);
        assertEquals(1, back.getId());
        assertEquals("dog", back.getName());
    }

    @Test
    void shouldMapPetTypeFieldsDtoToPetType_ignoreId() {
        PetTypeFieldsDto fieldsDto = new PetTypeFieldsDto();
        fieldsDto.setName("cat");

        PetType type = petTypeMapper.toPetType(fieldsDto);

        assertNotNull(type);
        assertNull(type.getId()); // ignored
        assertEquals("cat", type.getName());
    }

    @Test
    void shouldMapPetTypeToFieldsDto() {
        PetType type = new PetType();
        type.setId(5);
        type.setName("bird");

        PetTypeFieldsDto dto = petTypeMapper.toPetTypeFieldsDto(type);

        assertNotNull(dto);
        assertEquals("bird", dto.getName());
    }

    @Test
    void shouldMapPetTypeCollectionToDtoList() {
        PetType t1 = new PetType();
        t1.setId(1);
        t1.setName("dog");

        PetType t2 = new PetType();
        t2.setId(2);
        t2.setName("cat");

        List<PetTypeDto> list = petTypeMapper.toPetTypeDtos(List.of(t1, t2));

        assertNotNull(list);
        assertEquals(2, list.size());
    }
}
