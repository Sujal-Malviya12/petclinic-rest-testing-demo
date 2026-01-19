package org.springframework.samples.petclinic.rest.controller;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(RootRestController.class)
@AutoConfigureMockMvc(addFilters = false)   // ✅ IMPORTANT
class RootRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
void shouldReturnWelcomeMessage() throws Exception {
    mockMvc.perform(get("/").accept(MediaType.TEXT_PLAIN))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/swagger-ui/index.html"));
}

@Test
void shouldReturnWelcomeMessageJson() throws Exception {
    mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/swagger-ui/index.html"));
}

}
