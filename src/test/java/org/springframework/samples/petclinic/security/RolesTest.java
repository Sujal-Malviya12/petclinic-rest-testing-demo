package org.springframework.samples.petclinic.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class RolesTest {

    @Test
    void shouldContainCorrectRoleNames() {
        Roles roles = new Roles();
        assertEquals("ROLE_OWNER_ADMIN", roles.OWNER_ADMIN);
        assertEquals("ROLE_VET_ADMIN", roles.VET_ADMIN);
        assertEquals("ROLE_ADMIN", roles.ADMIN);
    }
}
