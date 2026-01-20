package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class BaseEntityTest {

    static class TestEntity extends BaseEntity {
    }

    @Test
    void shouldSetAndGetId() {
        TestEntity entity = new TestEntity();
        entity.setId(10);

        assertEquals(10, entity.getId());
        assertTrue(entity.isNew() == false);
    }

    @Test
    void shouldBeNewWhenIdIsNull() {
        TestEntity entity = new TestEntity();
        assertTrue(entity.isNew());
    }
}
