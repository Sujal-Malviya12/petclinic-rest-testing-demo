package org.springframework.samples.petclinic.mapper;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.dto.VetDto;

@SpringBootTest
class VetMapperTest {

    @Autowired
    private VetMapper vetMapper;

    @Test
    void shouldMapVetToDtoAndBack() {
        Vet vet = new Vet();
        vet.setId(11);
        vet.setFirstName("John");
        vet.setLastName("Doe");

        VetDto dto = vetMapper.toVetDto(vet);
        assertNotNull(dto);
        assertEquals(11, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());

        Vet back = vetMapper.toVet(dto);
        assertNotNull(back);
        assertEquals(11, back.getId());
        assertEquals("John", back.getFirstName());
        assertEquals("Doe", back.getLastName());
    }

    @Test
    void shouldMapVetList() {
        Vet v1 = new Vet();
        v1.setId(1);

        Vet v2 = new Vet();
        v2.setId(2);

        Collection<VetDto> collection = vetMapper.toVetDtos(List.of(v1, v2));
        assertNotNull(collection);
        assertEquals(2, collection.size());
    }
}
