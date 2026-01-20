package org.springframework.samples.petclinic.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * These tests improve coverage for:
 * - BasicAuthenticationConfig
 * - DisableSecurityConfig
 * - Roles
 *
 * We run the same app with different property values:
 * petclinic.security.enable=true  -> Basic Auth enabled (401 without auth)
 * petclinic.security.enable=false -> Security disabled (permit all)
 */
class SecurityConfigTests {

    @SpringBootTest(properties = "petclinic.security.enable=true")
    @AutoConfigureMockMvc
    static class WhenSecurityEnabled {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ApplicationContext context;

        @Test
        void shouldLoadBasicAuthenticationConfigBeans() {
            assertNotNull(context.getBean(BasicAuthenticationConfig.class));
            assertNotNull(context.getBean(PasswordEncoder.class));
        }

        @Test
        void requestWithoutAuth_shouldReturn401() throws Exception {
            // When security is enabled, any request needs authentication
            mockMvc.perform(get("/api/owners"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @SpringBootTest(properties = "petclinic.security.enable=false")
    @AutoConfigureMockMvc
    static class WhenSecurityDisabled {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ApplicationContext context;

        @Test
        void shouldLoadDisableSecurityConfigBeans() {
            assertNotNull(context.getBean(DisableSecurityConfig.class));
        }

        @Test
        void requestWithoutAuth_shouldReturn200or404_butNot401() throws Exception {
            // With disabled security, request should NOT be blocked by 401/403
            // Depending on data/test setup, API might return 200 OK or 404 Not Found,
            // but security should not reject it.
            int status = mockMvc.perform(get("/api/owners"))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            assertTrue(status == 200 || status == 404,
                    "Expected 200 or 404 when security disabled, but got: " + status);
        }
    }

    @Test
    void rolesClass_shouldHaveExpectedRoleNames() {
        Roles roles = new Roles();
        assertEquals("ROLE_OWNER_ADMIN", roles.OWNER_ADMIN);
        assertEquals("ROLE_VET_ADMIN", roles.VET_ADMIN);
        assertEquals("ROLE_ADMIN", roles.ADMIN);
    }
}
