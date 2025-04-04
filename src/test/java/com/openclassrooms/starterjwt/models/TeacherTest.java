package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    @Test
    public void testDefaultConstructor() {
        Teacher teacher = new Teacher();
        assertNull(teacher.getId());
        assertNull(teacher.getLastName());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getCreatedAt());
        assertNull(teacher.getUpdatedAt());
    }

    @Test
    public void testBuilderAndAllArgsConstructor() {
        // Prepare test data
        Long id = 1L;
        String lastName = "Doe";
        String firstName = "John";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Create teacher using builder
        Teacher teacher = Teacher.builder()
                .id(id)
                .lastName(lastName)
                .firstName(firstName)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Verify all properties
        assertEquals(id, teacher.getId());
        assertEquals(lastName, teacher.getLastName());
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(createdAt, teacher.getCreatedAt());
        assertEquals(updatedAt, teacher.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Create two teachers with the same ID
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        teacher2.setFirstName("Jane"); // Different name but same ID
        teacher2.setLastName("Smith");

        // Create a teacher with different ID
        Teacher teacher3 = new Teacher();
        teacher3.setId(2L);
        teacher3.setFirstName("John"); // Same first name but different ID
        teacher3.setLastName("Doe");   // Same last name but different ID

        // Test equals method
        assertEquals(teacher1, teacher2, "Teachers with same ID should be equal");
        assertNotEquals(teacher1, teacher3, "Teachers with different ID should not be equal");

        // Test hashCode
        assertEquals(teacher1.hashCode(), teacher2.hashCode(), "HashCodes should be equal for equal teachers");
        assertNotEquals(teacher1.hashCode(), teacher3.hashCode(), "HashCodes should differ for different teachers");
    }

    @Test
    public void testToString() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        String toString = teacher.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("id=1"));
    }
}