package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetClinicApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainRuns() {
        PetClinicApplication.main(new String[]{});
    }
}
