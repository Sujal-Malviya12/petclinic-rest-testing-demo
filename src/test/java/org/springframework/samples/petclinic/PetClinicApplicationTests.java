package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test for PetClinicApplication to ensure Spring Boot application context is properly initialized
 */
@SpringBootTest
class PetClinicApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void petClinicApplicationBeanExists() {
        assertNotNull(applicationContext.getBean(PetClinicApplication.class));
    }

    @Test
    void applicationStartsSuccessfully() {
        // This test verifies that the application can start without errors
        assertNotNull(applicationContext);
        // Verify that key beans are loaded
        assertNotNull(applicationContext.getBean(PetClinicApplication.class));
    }
}
