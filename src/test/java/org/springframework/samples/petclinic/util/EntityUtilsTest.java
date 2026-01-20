package org.springframework.samples.petclinic.util;

import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Owner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityUtilsTest {

    @Test
    void getById_shouldReturnEntity_whenEntityExists() {
        // Arrange
        Owner o1 = new Owner();
        o1.setId(1);

        Owner o2 = new Owner();
        o2.setId(2);

        List<Owner> owners = new ArrayList<>();
        owners.add(o1);
        owners.add(o2);

        // Act
        Owner result = EntityUtils.getById(owners, Owner.class, 2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getId());
    }

    @Test
    void getById_shouldThrowException_whenEntityNotFound() {
        // Arrange
        Owner o1 = new Owner();
        o1.setId(1);

        List<Owner> owners = List.of(o1);

        // Act + Assert
        assertThrows(ObjectRetrievalFailureException.class,
                () -> EntityUtils.getById(owners, Owner.class, 999));
    }
}
