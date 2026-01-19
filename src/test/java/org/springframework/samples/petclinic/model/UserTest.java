package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void shouldSetAndGetUserFields() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("pass123");
        user.setEnabled(true);

        assertEquals("admin", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertTrue(user.getEnabled());
    }
}
