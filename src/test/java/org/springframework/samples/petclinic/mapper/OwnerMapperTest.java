package org.springframework.samples.petclinic.mapper;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto;

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
        assertEquals("Street 1", dto.getAddress());
        assertEquals("9999999999", dto.getTelephone());
    }

    @Test
    void shouldMapOwnerDtoToOwner() {
        OwnerDto dto = new OwnerDto();
        dto.setId(20);
        dto.setFirstName("A");
        dto.setLastName("B");
        dto.setAddress("Addr");
        dto.setCity("City");
        dto.setTelephone("123");

        Owner owner = ownerMapper.toOwner(dto);

        assertNotNull(owner);
        assertEquals(20, owner.getId());
        assertEquals("A", owner.getFirstName());
        assertEquals("B", owner.getLastName());
        assertEquals("Addr", owner.getAddress());
        assertEquals("City", owner.getCity());
        assertEquals("123", owner.getTelephone());
    }

    @Test
    void shouldMapOwnerFieldsDtoToOwner_andIgnoreIdAndPets() {
        OwnerFieldsDto fieldsDto = new OwnerFieldsDto();
        fieldsDto.setFirstName("New");
        fieldsDto.setLastName("Owner");
        fieldsDto.setAddress("Somewhere");
        fieldsDto.setCity("Bhopal");
        fieldsDto.setTelephone("7777777777");

        Owner owner = ownerMapper.toOwner(fieldsDto);

        assertNotNull(owner);

        // id ignored by mapping
        assertNull(owner.getId());

        // pets ignored by mapping (should be empty list in entity)
        // Owner#getPets() returns a Collection, can be empty
        assertTrue(owner.getPets() == null || owner.getPets().isEmpty());

        assertEquals("New", owner.getFirstName());
        assertEquals("Owner", owner.getLastName());
        assertEquals("Somewhere", owner.getAddress());
        assertEquals("Bhopal", owner.getCity());
        assertEquals("7777777777", owner.getTelephone());
    }

    @Test
    void shouldMapOwnerCollectionToOwnerDtoList() {
        Owner o1 = new Owner();
        o1.setId(1);
        o1.setFirstName("One");

        Owner o2 = new Owner();
        o2.setId(2);
        o2.setFirstName("Two");

        Collection<Owner> owners = List.of(o1, o2);

        List<OwnerDto> dtoList = ownerMapper.toOwnerDtoCollection(owners);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(1, dtoList.get(0).getId());
        assertEquals(2, dtoList.get(1).getId());
    }

    @Test
    void shouldMapOwnerDtoCollectionToOwnerCollection() {
        OwnerDto d1 = new OwnerDto();
        d1.setId(1);
        d1.setFirstName("AAA");

        OwnerDto d2 = new OwnerDto();
        d2.setId(2);
        d2.setFirstName("BBB");

        Collection<OwnerDto> dtos = List.of(d1, d2);

        Collection<Owner> owners = ownerMapper.toOwners(dtos);

        assertNotNull(owners);
        assertEquals(2, owners.size());

        // verify ids exist in mapped result
        List<Integer> ids = owners.stream().map(Owner::getId).toList();
        assertTrue(ids.contains(1));
        assertTrue(ids.contains(2));
    }

    @Test
    void shouldMapOwnerWithPets_toOwnerDto() {
        // This will cover nested mapping via @Mapper(uses = PetMapper.class)
        Owner owner = new Owner();
        owner.setId(99);
        owner.setFirstName("George");
        owner.setLastName("Franklin");

        Pet pet = new Pet();
        pet.setName("Tommy");
        owner.addPet(pet);

        OwnerDto dto = ownerMapper.toOwnerDto(owner);

        assertNotNull(dto);
        assertEquals(99, dto.getId());

        // pets mapping should not crash
        if (dto.getPets() != null) {
            assertFalse(dto.getPets().isEmpty());
        }
    }
}
