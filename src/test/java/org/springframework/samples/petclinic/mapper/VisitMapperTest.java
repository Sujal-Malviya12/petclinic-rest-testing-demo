package org.springframework.samples.petclinic.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.dto.VisitDto;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VisitMapperTest {

    @Autowired
    private VisitMapper visitMapper;

    @Test
    void shouldMapVisitToDtoAndBack() {
        Visit visit = new Visit();
        visit.setId(100);
        visit.setDate(LocalDate.of(2024, 1, 1));
        visit.setDescription("checkup");

        VisitDto dto = visitMapper.toVisitDto(visit);
        assertNotNull(dto);
        assertEquals(100, dto.getId());
        assertEquals("checkup", dto.getDescription());

        Visit back = visitMapper.toVisit(dto);
        assertNotNull(back);
        assertEquals(100, back.getId());
        assertEquals("checkup", back.getDescription());
    }

    @Test
    void shouldMapVisitListUsingStream() {
        Visit v1 = new Visit();
        v1.setId(1);

        Visit v2 = new Visit();
        v2.setId(2);

        List<VisitDto> list = List.of(v1, v2).stream()
                .map(visitMapper::toVisitDto)
                .toList();

        assertEquals(2, list.size());
    }
}
