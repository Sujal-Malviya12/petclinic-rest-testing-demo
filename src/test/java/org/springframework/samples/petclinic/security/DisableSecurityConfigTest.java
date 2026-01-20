package org.springframework.samples.petclinic.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(properties = "petclinic.security.enable=false")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class DisableSecurityConfigTest {

    @Autowired
    ApplicationContext context;

    @Test
    void shouldLoadDisableSecurityConfigBean() {
        assertNotNull(context.getBean(DisableSecurityConfig.class));
    }
}
