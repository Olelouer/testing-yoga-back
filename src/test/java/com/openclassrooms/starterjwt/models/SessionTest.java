package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    @Test
    public void testDefaultConstructor() {
        Session session = new Session();
        assertNull(session.getId());
        assertNull(session.getName());
        assertNull(session.getDate());
        assertNull(session.getDescription());
        assertNull(session.getTeacher());
        assertNull(session.getUsers());
        assertNull(session.getCreatedAt());
        assertNull(session.getUpdatedAt());
    }

    @Test
    public void testBuilderAndAllArgsConstructor() {
        // Prepare test data
        Long id = 1L;
        String name = "Yoga Class";
        Date date = new Date();
        String description = "A relaxing yoga session for beginners";
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Create session using builder
        Session session = Session.builder()
                .id(id)
                .name(name)
                .date(date)
                .description(description)
                .teacher(teacher)
                .users(users)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Verify all properties
        assertEquals(id, session.getId());
        assertEquals(name, session.getName());
        assertEquals(date, session.getDate());
        assertEquals(description, session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(createdAt, session.getCreatedAt());
        assertEquals(updatedAt, session.getUpdatedAt());
    }

    @Test
    public void testSettersAndGetters() {
        // Create a new session
        Session session = new Session();

        // Prepare test data
        Long id = 2L;
        String name = "Pilates Class";
        Date date = new Date();
        String description = "Core strengthening exercises";
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Set properties using setters
        session.setId(id);
        session.setName(name);
        session.setDate(date);
        session.setDescription(description);
        session.setTeacher(teacher);
        session.setUsers(users);
        session.setCreatedAt(createdAt);
        session.setUpdatedAt(updatedAt);

        // Verify properties using getters
        assertEquals(id, session.getId());
        assertEquals(name, session.getName());
        assertEquals(date, session.getDate());
        assertEquals(description, session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(createdAt, session.getCreatedAt());
        assertEquals(updatedAt, session.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Create two sessions with the same ID
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Class 1");

        Session session2 = new Session();
        session2.setId(1L);
        session2.setName("Class 2"); // Different name but same ID

        // Create a session with different ID
        Session session3 = new Session();
        session3.setId(2L);
        session3.setName("Class 1"); // Same name but different ID

        // Test equals method
        assertEquals(session1, session2, "Sessions with same ID should be equal");
        assertNotEquals(session1, session3, "Sessions with different ID should not be equal");

        // Test hashCode
        assertEquals(session1.hashCode(), session2.hashCode(), "HashCodes should be equal for equal sessions");
        assertNotEquals(session1.hashCode(), session3.hashCode(), "HashCodes should differ for different sessions");
    }

    @Test
    public void testChaining() {
        // Test chaining methods
        Date date = new Date();
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();

        Session session = new Session()
                .setId(1L)
                .setName("Chained Session")
                .setDate(date)
                .setDescription("Description set via chaining")
                .setTeacher(teacher)
                .setUsers(users);

        assertEquals(1L, session.getId());
        assertEquals("Chained Session", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Description set via chaining", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
    }

    @Test
    public void testToString() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");

        String toString = session.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Test Session"));
        assertTrue(toString.contains("id=1"));
    }

    @Test
    public void testBuilderToString() {
        // Test toString() method on the builder
        String builderString = Session.builder().toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("Session.SessionBuilder"));
    }
}