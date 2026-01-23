package org.springframework.samples.petclinic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class JunitDemoTest {
    
    @Test
    void SampleTest()
    {
        assertEquals(5,5+5);
    }
}
