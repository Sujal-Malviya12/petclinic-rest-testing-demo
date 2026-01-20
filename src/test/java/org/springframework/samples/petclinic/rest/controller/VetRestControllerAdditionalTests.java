package org.springframework.samples.petclinic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.mapper.VetMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;
import org.springframework.samples.petclinic.rest.dto.VetDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Additional test class for {@link VetRestController} to increase coverage
 */
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
class VetRestControllerAdditionalTests {

    @Autowired
    private VetRestController vetRestController;

    @Autowired
    private VetMapper vetMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<Vet> vets;
    private List<Specialty> specialties;

    @BeforeEach
    void initVets() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(vetRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();

        vets = new ArrayList<>();
        specialties = new ArrayList<>();

        Specialty specialty1 = new Specialty();
        specialty1.setId(1);
        specialty1.setName("radiology");
        specialties.add(specialty1);

        Specialty specialty2 = new Specialty();
        specialty2.setId(2);
        specialty2.setName("surgery");
        specialties.add(specialty2);

        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");
        vet.addSpecialty(specialty1);
        vets.add(vet);

        vet = new Vet();
        vet.setId(2);
        vet.setFirstName("Helen");
        vet.setLastName("Leary");
        vet.addSpecialty(specialty2);
        vets.add(vet);

        vet = new Vet();
        vet.setId(3);
        vet.setFirstName("Linda");
        vet.setLastName("Douglas");
        vets.add(vet);
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testCreateVetWithSpecialties() throws Exception {
        Vet newVet = new Vet();
        newVet.setId(999);
        newVet.setFirstName("John");
        newVet.setLastName("Smith");
        newVet.addSpecialty(specialties.get(0));

        VetDto vetDto = vetMapper.toVetDto(newVet);

        given(this.clinicService.findSpecialtiesByNameIn(anySet()))
                .willReturn(List.of(specialties.get(0)));

        ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetDto);

        this.mockMvc.perform(post("/api/vets")
                .content(newVetAsJSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testCreateVetWithoutSpecialties() throws Exception {
        Vet newVet = new Vet();
        newVet.setId(999);
        newVet.setFirstName("John");
        newVet.setLastName("Smith");

        VetDto vetDto = vetMapper.toVetDto(newVet);

        ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetDto);

        this.mockMvc.perform(post("/api/vets")
                .content(newVetAsJSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testUpdateVetWithSpecialties() throws Exception {
        Vet vetToUpdate = new Vet();
        vetToUpdate.setId(1);
        vetToUpdate.setFirstName("James");
        vetToUpdate.setLastName("Carter");
        vetToUpdate.addSpecialty(specialties.get(0));

        given(this.clinicService.findVetById(1)).willReturn(vetToUpdate);
        given(this.clinicService.findSpecialtiesByNameIn(anySet()))
                .willReturn(List.of(specialties.get(0)));

        VetDto vetDto = vetMapper.toVetDto(vetToUpdate);
        ObjectMapper mapper = new ObjectMapper();
        String vetAsJSON = mapper.writeValueAsString(vetDto);

        this.mockMvc.perform(put("/api/vets/1")
                .content(vetAsJSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testUpdateVetNotFound() throws Exception {
        Vet vetToUpdate = new Vet();
        vetToUpdate.setId(999);
        vetToUpdate.setFirstName("NonExistent");
        vetToUpdate.setLastName("Vet");

        given(this.clinicService.findVetById(999)).willReturn(null);

        VetDto vetDto = vetMapper.toVetDto(vetToUpdate);
        ObjectMapper mapper = new ObjectMapper();
        String vetAsJSON = mapper.writeValueAsString(vetDto);

        this.mockMvc.perform(put("/api/vets/999")
                .content(vetAsJSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testUpdateVetWithoutSpecialties() throws Exception {
        Vet vetToUpdate = new Vet();
        vetToUpdate.setId(1);
        vetToUpdate.setFirstName("James");
        vetToUpdate.setLastName("Carter");

        given(this.clinicService.findVetById(1)).willReturn(vetToUpdate);

        VetDto vetDto = vetMapper.toVetDto(vetToUpdate);
        ObjectMapper mapper = new ObjectMapper();
        String vetAsJSON = mapper.writeValueAsString(vetDto);

        this.mockMvc.perform(put("/api/vets/1")
                .content(vetAsJSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testDeleteVetNotFound() throws Exception {
        given(this.clinicService.findVetById(999)).willReturn(null);

        Vet vet = new Vet();
        ObjectMapper mapper = new ObjectMapper();
        String vetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(vet));

        this.mockMvc.perform(delete("/api/vets/999")
                .content(vetAsJSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}
