package org.springframework.samples.petclinic.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.rest.dto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional tests for Mappers to increase branch coverage
 */
@SpringBootTest
class MapperAdditionalTests {

    @Autowired
    private VetMapper vetMapper;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private VisitMapper visitMapper;

    @Test
    void testVetMapperWithNullSpecialties() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");
        vet.setSpecialties(new ArrayList<>());

        VetDto dto = vetMapper.toVetDto(vet);
        assertNotNull(dto);
        assertEquals(0, dto.getSpecialties().size());
    }

    @Test
    void testVetMapperWithMultipleSpecialties() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");

        Specialty s1 = new Specialty();
        s1.setId(1);
        s1.setName("surgery");

        Specialty s2 = new Specialty();
        s2.setId(2);
        s2.setName("radiology");

        vet.addSpecialty(s1);
        vet.addSpecialty(s2);

        VetDto dto = vetMapper.toVetDto(vet);
        assertNotNull(dto);
        assertEquals(2, dto.getSpecialties().size());
    }

    @Test
    void testOwnerMapperNull() {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("Jane");
        owner.setLastName("Smith");
        owner.setAddress("123 Main St");
        owner.setCity("New York");
        owner.setTelephone("5551234567");
        owner.setPets(new ArrayList<>());

        OwnerDto dto = ownerMapper.toOwnerDto(owner);
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Jane", dto.getFirstName());
        assertEquals(0, dto.getPets().size());
    }

    @Test
    void testOwnerMapperWithPets() {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("Jane");
        owner.setLastName("Smith");
        owner.setAddress("123 Main St");
        owner.setCity("New York");
        owner.setTelephone("5551234567");

        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Fluffy");
        pet.setOwner(owner);
        pet.setBirthDate(LocalDate.now());

        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("cat");
        pet.setType(petType);

        owner.addPet(pet);

        OwnerDto dto = ownerMapper.toOwnerDto(owner);
        assertNotNull(dto);
        assertEquals(1, dto.getPets().size());
    }

    @Test
    void testPetMapperBasic() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Fluffy");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));

        PetType type = new PetType();
        type.setId(1);
        type.setName("cat");
        pet.setType(type);

        PetDto dto = petMapper.toPetDto(pet);
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Fluffy", dto.getName());
    }

    @Test
    void testPetMapperCollection() {
        Pet pet1 = new Pet();
        pet1.setId(1);
        pet1.setName("Fluffy");

        Pet pet2 = new Pet();
        pet2.setId(2);
        pet2.setName("Spot");

        Collection<PetDto> dtos = petMapper.toPetsDto(List.of(pet1, pet2));
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void testPetTypMapperBasic() {
        PetType type = new PetType();
        type.setId(1);
        type.setName("dog");

        PetTypeDto dto = petTypeMapper.toPetTypeDto(type);
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("dog", dto.getName());
    }

    @Test
    void testPetTypeMapperNull() {
        PetTypeDto dto = petTypeMapper.toPetTypeDto(null);
        assertNull(dto);
    }

    @Test
    void testPetTypeMapperCollection() {
        PetType type1 = new PetType();
        type1.setId(1);
        type1.setName("dog");

        PetType type2 = new PetType();
        type2.setId(2);
        type2.setName("cat");

        Collection<PetTypeDto> dtos = petTypeMapper.toPetTypeDtos(List.of(type1, type2));
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void testSpecialtyMapperBasic() {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("surgery");

        SpecialtyDto dto = specialtyMapper.toSpecialtyDto(specialty);
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("surgery", dto.getName());
    }

    @Test
    void testSpecialtyMapperCollection() {
        Specialty s1 = new Specialty();
        s1.setId(1);
        s1.setName("surgery");

        Specialty s2 = new Specialty();
        s2.setId(2);
        s2.setName("radiology");

        Collection<SpecialtyDto> dtos = specialtyMapper.toSpecialtyDtos(List.of(s1, s2));
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void testVisitMapperBasic() {
        Visit visit = new Visit();
        visit.setId(1);
        visit.setDescription("Regular checkup");
        visit.setDate(LocalDate.now());

        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Fluffy");
        visit.setPet(pet);

        VisitDto dto = visitMapper.toVisitDto(visit);
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Regular checkup", dto.getDescription());
    }

    @Test
    void testVisitMapperCollection() {
        Visit visit1 = new Visit();
        visit1.setId(1);
        visit1.setDescription("Checkup");

        Visit visit2 = new Visit();
        visit2.setId(2);
        visit2.setDescription("Vaccination");

        Collection<VisitDto> dtos = visitMapper.toVisitsDto(List.of(visit1, visit2));
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void testOwnerMapperDtoToOwner() {
        OwnerDto dto = new OwnerDto();
        dto.setId(1);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("123 Main St");
        dto.setCity("Boston");
        dto.setTelephone("6175551234");

        Owner owner = ownerMapper.toOwner(dto);
        assertNotNull(owner);
        assertEquals(1, owner.getId());
        assertEquals("John", owner.getFirstName());
    }

    @Test
    void testPetMapperDtoToPet() {
        PetDto dto = new PetDto();
        dto.setId(1);
        dto.setName("Fluffy");
        dto.setBirthDate(LocalDate.of(2020, 1, 1));

        PetTypeDto typeDto = new PetTypeDto();
        typeDto.setId(1);
        typeDto.setName("cat");
        dto.setType(typeDto);

        Pet pet = petMapper.toPet(dto);
        assertNotNull(pet);
        assertEquals(1, pet.getId());
        assertEquals("Fluffy", pet.getName());
    }

    @Test
    void testPetTypeMapperDtoToPetType() {
        PetTypeDto dto = new PetTypeDto();
        dto.setId(1);
        dto.setName("dog");

        PetType type = petTypeMapper.toPetType(dto);
        assertNotNull(type);
        assertEquals(1, type.getId());
        assertEquals("dog", type.getName());
    }

    @Test
    void testSpecialtyMapperDtoToSpecialty() {
        SpecialtyDto dto = new SpecialtyDto();
        dto.setId(1);
        dto.setName("surgery");

        Specialty specialty = specialtyMapper.toSpecialty(dto);
        assertNotNull(specialty);
        assertEquals(1, specialty.getId());
        assertEquals("surgery", specialty.getName());
    }

    @Test
    void testVisitMapperDtoToVisit() {
        VisitDto dto = new VisitDto();
        dto.setId(1);
        dto.setDescription("Regular checkup");
        dto.setDate(LocalDate.now());

        Visit visit = visitMapper.toVisit(dto);
        assertNotNull(visit);
        assertEquals(1, visit.getId());
        assertEquals("Regular checkup", visit.getDescription());
    }
}
