package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testDefaultConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getLastName());
        assertNull(user.getFirstName());
        assertNull(user.getPassword());
        assertFalse(user.isAdmin()); // boolean defaults to false
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    public void testRequiredArgsConstructor() {
        // Create user with required args constructor (all @NonNull fields)
        User user = new User(
                "john@example.com",
                "Doe",
                "John",
                "password123",
                false
        );

        assertNull(user.getId()); // ID is not set by required args constructor
        assertEquals("john@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    public void testBuilderAndAllArgsConstructor() {
        // Prepare test data
        Long id = 1L;
        String email = "jane@example.com";
        String lastName = "Smith";
        String firstName = "Jane";
        String password = "securePassword";
        boolean admin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Create user using builder
        User user = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(admin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Verify all properties
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(lastName, user.getLastName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(password, user.getPassword());
        assertEquals(admin, user.isAdmin());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Create two users with the same ID
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("different@example.com"); // Different email but same ID

        // Create a user with different ID
        User user3 = new User();
        user3.setId(2L);
        user3.setEmail("user1@example.com"); // Same email but different ID

        // Test equals method
        assertEquals(user1, user2, "Users with same ID should be equal");
        assertNotEquals(user1, user3, "Users with different ID should not be equal");

        // Test hashCode
        assertEquals(user1.hashCode(), user2.hashCode(), "HashCodes should be equal for equal users");
        assertNotEquals(user1.hashCode(), user3.hashCode(), "HashCodes should differ for different users");
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setId(1L);
        user.setEmail("toString@example.com");
        user.setFirstName("ToString");
        user.setLastName("Test");

        String toString = user.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("toString@example.com"));
        assertTrue(toString.contains("ToString"));
        assertTrue(toString.contains("Test"));
        assertTrue(toString.contains("id=1"));
    }

    @Test
    public void testNonNullFields() {
        // This test verifies that @NonNull fields throw NullPointerException when set to null
        User user = new User();

        // Email is @NonNull
        assertThrows(NullPointerException.class, () -> {
            user.setEmail(null);
        });

        // LastName is @NonNull
        assertThrows(NullPointerException.class, () -> {
            user.setLastName(null);
        });

        // FirstName is @NonNull
        assertThrows(NullPointerException.class, () -> {
            user.setFirstName(null);
        });

        // Password is @NonNull
        assertThrows(NullPointerException.class, () -> {
            user.setPassword(null);
        });
    }
}