package org.springframework.samples.petclinic.mapper;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.dto.VetDto;
import org.springframework.samples.petclinic.rest.dto.VetFieldsDto;

@SpringBootTest
class VetMapperTest {

    @Autowired
    private VetMapper vetMapper;

    @Test
    void shouldMapVetToVetDto() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");

        VetDto dto = vetMapper.toVetDto(vet);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
    }

    @Test
    void shouldMapVetDtoToVet() {
        VetDto dto = new VetDto();
        dto.setId(2);
        dto.setFirstName("A");
        dto.setLastName("B");

        Vet vet = vetMapper.toVet(dto);

        assertNotNull(vet);
        assertEquals(2, vet.getId());
        assertEquals("A", vet.getFirstName());
        assertEquals("B", vet.getLastName());
    }

    @Test
    void shouldMapVetFieldsDtoToVet_ignoreId() {
        VetFieldsDto fieldsDto = new VetFieldsDto();
        fieldsDto.setFirstName("X");
        fieldsDto.setLastName("Y");

        Vet vet = vetMapper.toVet(fieldsDto);

        assertNotNull(vet);
        assertNull(vet.getId()); // ignored
        assertEquals("X", vet.getFirstName());
        assertEquals("Y", vet.getLastName());
    }

    @Test
    void shouldMapVetCollectionToDtoCollection() {
        Vet v1 = new Vet();
        v1.setId(1);

        Vet v2 = new Vet();
        v2.setId(2);

        Collection<VetDto> result = vetMapper.toVetDtos(List.of(v1, v2));

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
