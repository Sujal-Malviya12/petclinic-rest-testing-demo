package org.springframework.samples.petclinic.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(properties = "petclinic.security.enable=true")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BasicAuthenticationConfigTest {

    @Autowired
    ApplicationContext context;

    @Test
    void shouldLoadBasicAuthenticationConfigBean() {
        assertNotNull(context.getBean(BasicAuthenticationConfig.class));
    }

    @Test
    void shouldCreatePasswordEncoder() {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        assertNotNull(encoder);
        assertTrue(encoder.matches("pass", encoder.encode("pass")));
    }
}
