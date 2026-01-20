package org.springframework.samples.petclinic.mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto;

@SpringBootTest
class VisitMapperTest {

    @Autowired
    private VisitMapper visitMapper;

    @Test
    void shouldMapVisitDtoToVisit_withPetId() {
        VisitDto dto = new VisitDto();
        dto.setId(10);
        dto.setPetId(55);
        dto.setDate(LocalDate.of(2024, 1, 1));
        dto.setDescription("checkup");

        Visit visit = visitMapper.toVisit(dto);

        assertNotNull(visit);
        assertEquals(10, visit.getId());
        assertNotNull(visit.getPet());
        assertEquals(55, visit.getPet().getId());
        assertEquals("checkup", visit.getDescription());
    }

    @Test
    void shouldMapVisitFieldsDtoToVisit_ignoreIdAndPet() {
        VisitFieldsDto fieldsDto = new VisitFieldsDto();
        fieldsDto.setDate(LocalDate.of(2024, 2, 2));
        fieldsDto.setDescription("vaccination");

        Visit visit = visitMapper.toVisit(fieldsDto);

        assertNotNull(visit);

        // ignored
        assertNull(visit.getId());
        assertNull(visit.getPet());

        assertEquals("vaccination", visit.getDescription());
    }

    @Test
    void shouldMapVisitToVisitDto_withPetId() {
        Pet pet = new Pet();
        pet.setId(99);

        Visit visit = new Visit();
        visit.setId(1);
        visit.setPet(pet);
        visit.setDate(LocalDate.of(2024, 3, 3));
        visit.setDescription("test");

        VisitDto dto = visitMapper.toVisitDto(visit);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(99, dto.getPetId());
        assertEquals("test", dto.getDescription());
    }

    @Test
    void shouldMapVisitCollectionToDtoCollection() {
        Visit v1 = new Visit();
        v1.setId(1);

        Visit v2 = new Visit();
        v2.setId(2);

        Collection<VisitDto> result = visitMapper.toVisitsDto(List.of(v1, v2));

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
