package org.springframework.samples.petclinic.mapper;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;

@SpringBootTest
class SpecialtyMapperTest {

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Test
    void shouldMapSpecialtyToDtoAndBack() {
        Specialty sp = new Specialty();
        sp.setId(7);
        sp.setName("surgery");

        SpecialtyDto dto = specialtyMapper.toSpecialtyDto(sp);
        assertNotNull(dto);
        assertEquals(7, dto.getId());
        assertEquals("surgery", dto.getName());

        Specialty back = specialtyMapper.toSpecialty(dto);
        assertNotNull(back);
        assertEquals(7, back.getId());
        assertEquals("surgery", back.getName());
    }

    @Test
    void shouldMapSpecialtyList() {
        Specialty s1 = new Specialty();
        s1.setId(1);
        s1.setName("a");

        Specialty s2 = new Specialty();
        s2.setId(2);
        s2.setName("b");

        Collection<SpecialtyDto> collection = specialtyMapper.toSpecialtyDtos(List.of(s1, s2));
        assertNotNull(collection);
        assertEquals(2, collection.size());
    }
}
