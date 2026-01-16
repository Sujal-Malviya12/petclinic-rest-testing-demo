package org.springframework.samples.petclinic.mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;

@SpringBootTest
class OwnerMapperTest {

    @Autowired
    private OwnerMapper ownerMapper;

    @Test
    void shouldMapOwnerToOwnerDto() {
        Owner owner = new Owner();
        owner.setId(10);
        owner.setFirstName("Sujal");
        owner.setLastName("Malviya");
        owner.setAddress("Street 1");
        owner.setCity("Indore");
        owner.setTelephone("9999999999");

        OwnerDto dto = ownerMapper.toOwnerDto(owner);

        assertNotNull(dto);
        assertEquals(10, dto.getId());
        assertEquals("Sujal", dto.getFirstName());
        assertEquals("Malviya", dto.getLastName());
        assertEquals("Indore", dto.getCity());
    }

    @Test
    void shouldMapOwnerDtoToOwner() {
        OwnerDto dto = new OwnerDto();
        dto.setId(20);
        dto.setFirstName("A");
        dto.setLastName("B");
        dto.setCity("City");
        dto.setTelephone("123");

        Owner owner = ownerMapper.toOwner(dto);

        assertNotNull(owner);
        assertEquals(20, owner.getId());
        assertEquals("A", owner.getFirstName());
        assertEquals("B", owner.getLastName());
    }

    @Test
    void shouldMapOwnerListUsingStream() {
        Owner o1 = new Owner();
        o1.setId(1);
        o1.setFirstName("One");

        Owner o2 = new Owner();
        o2.setId(2);
        o2.setFirstName("Two");

        List<OwnerDto> list = List.of(o1, o2).stream()
                .map(ownerMapper::toOwnerDto)
                .toList();

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(2, list.get(1).getId());
    }
}
