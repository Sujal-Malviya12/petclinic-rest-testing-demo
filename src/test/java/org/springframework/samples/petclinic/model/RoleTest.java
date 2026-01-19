package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void shouldSetAndGetRoleName() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void shouldSetAndGetUser() {
        Role role = new Role();

        User user = new User();
        user.setUsername("admin");

        role.setUser(user);

        assertNotNull(role.getUser());
        assertEquals("admin", role.getUser().getUsername());
    }
}
