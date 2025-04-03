package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user1, user2;

    @BeforeEach
    public void setUp() {
        // Initialisation des objets de test
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setLastName("User");
        user1.setFirstName("One");
        user1.setPassword("password");
        user1.setAdmin(false);

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setLastName("User");
        user2.setFirstName("Two");
        user2.setPassword("password");
        user2.setAdmin(false);

        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session");
        session.setUsers(new ArrayList<>(Arrays.asList(user1)));
    }

    @Test
    public void testCreate() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        Session createdSession = sessionService.create(session);

        // Assert
        assertNotNull(createdSession);
        assertEquals(session.getId(), createdSession.getId());
        assertEquals(session.getName(), createdSession.getName());
        verify(sessionRepository).save(session);
    }

    @Test
    public void testDelete() {
        // Arrange
        Long sessionId = 1L;
        doNothing().when(sessionRepository).deleteById(sessionId);

        // Act
        sessionService.delete(sessionId);

        // Assert
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Session> sessions = Arrays.asList(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act
        List<Session> result = sessionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(session.getId(), result.get(0).getId());
        verify(sessionRepository).findAll();
    }

    @Test
    public void testGetByIdExisting() {
        // Arrange
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act
        Session result = sessionService.getById(sessionId);

        // Assert
        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    public void testGetByIdNonExisting() {
        // Arrange
        Long sessionId = 99L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act
        Session result = sessionService.getById(sessionId);

        // Assert
        assertNull(result);
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    public void testUpdate() {
        // Arrange
        Long sessionId = 1L;
        Session updatedSession = new Session();
        updatedSession.setName("Updated Yoga Session");
        updatedSession.setDescription("Updated description");

        when(sessionRepository.save(any(Session.class))).thenReturn(updatedSession);

        // Act
        Session result = sessionService.update(sessionId, updatedSession);

        // Assert
        assertNotNull(result);
        assertEquals(updatedSession.getName(), result.getName());
        assertEquals(sessionId, updatedSession.getId()); // Vérifie que l'ID a bien été défini
        verify(sessionRepository).save(updatedSession);
    }

    @Test
    public void testParticipateSuccess() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user2));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        sessionService.participate(sessionId, userId);

        // Assert
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository).save(session);

        // Vérifie que l'utilisateur a bien été ajouté à la session
        assertTrue(session.getUsers().contains(user2));
    }

    @Test
    public void testParticipateSessionNotFound() {
        // Arrange
        Long sessionId = 99L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });

        verify(sessionRepository).findById(sessionId);
        // L'implémentation appelle userRepository.findById même si la session n'est pas trouvée
        verify(userRepository).findById(userId);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testParticipateUserNotFound() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 99L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testParticipateAlreadyParticipating() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 1L; // user1 participe déjà

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(sessionId, userId);
        });

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testNoLongerParticipateSuccess() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 1L; // user1 participe déjà

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        sessionService.noLongerParticipate(sessionId, userId);

        // Assert
        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(session);

        // Vérifie que l'utilisateur a bien été retiré de la session
        assertFalse(session.getUsers().stream().anyMatch(u -> u.getId().equals(userId)));
    }

    @Test
    public void testNoLongerParticipateSessionNotFound() {
        // Arrange
        Long sessionId = 99L;
        Long userId = 1L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testNoLongerParticipateNotParticipating() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L; // user2 ne participe pas

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any());
    }
}