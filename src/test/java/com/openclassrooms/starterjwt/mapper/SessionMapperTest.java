package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private Teacher teacher;
    private User user1, user2;
    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    public void setUp() {
        // Injection manuelle des services dans le mapper
        ReflectionTestUtils.setField(sessionMapper, "teacherService", teacherService);
        ReflectionTestUtils.setField(sessionMapper, "userService", userService);

        // Création des données de test
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setPassword("password");
        user1.setAdmin(false);

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setPassword("password");
        user2.setAdmin(false);

        // Préparation de l'objet Session
        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDate(new Date());
        session.setDescription("A relaxing yoga session");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
        LocalDateTime now = LocalDateTime.now();
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        // Préparation de l'objet SessionDto
        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("A relaxing yoga session");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        sessionDto.setCreatedAt(now);
        sessionDto.setUpdatedAt(now);
    }

    @Test
    public void testToDto() {
        // Exécution du test
        SessionDto resultDto = sessionMapper.toDto(session);

        // Vérifications
        assertNotNull(resultDto);
        assertEquals(session.getId(), resultDto.getId());
        assertEquals(session.getName(), resultDto.getName());
        assertEquals(session.getDate(), resultDto.getDate());
        assertEquals(session.getDescription(), resultDto.getDescription());
        assertEquals(session.getTeacher().getId(), resultDto.getTeacher_id());
        assertEquals(session.getCreatedAt(), resultDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), resultDto.getUpdatedAt());

        // Vérification de la liste des utilisateurs
        assertNotNull(resultDto.getUsers());
        assertEquals(2, resultDto.getUsers().size());
        assertTrue(resultDto.getUsers().contains(1L));
        assertTrue(resultDto.getUsers().contains(2L));
    }

    @Test
    public void testToEntity() {
        // Configuration des mocks
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Exécution du test
        Session resultSession = sessionMapper.toEntity(sessionDto);

        // Vérifications
        assertNotNull(resultSession);
        assertEquals(sessionDto.getId(), resultSession.getId());
        assertEquals(sessionDto.getName(), resultSession.getName());
        assertEquals(sessionDto.getDate(), resultSession.getDate());
        assertEquals(sessionDto.getDescription(), resultSession.getDescription());
        assertEquals(sessionDto.getCreatedAt(), resultSession.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), resultSession.getUpdatedAt());

        // Vérification du teacher
        assertNotNull(resultSession.getTeacher());
        assertEquals(teacher.getId(), resultSession.getTeacher().getId());

        // Vérification de la liste des utilisateurs
        assertNotNull(resultSession.getUsers());
        assertEquals(2, resultSession.getUsers().size());
        assertEquals(user1.getId(), resultSession.getUsers().get(0).getId());
        assertEquals(user2.getId(), resultSession.getUsers().get(1).getId());

        // Vérification des appels de méthodes
        verify(teacherService).findById(1L);
        verify(userService).findById(1L);
        verify(userService).findById(2L);
    }

    @Test
    public void testToDtoWithNullTeacher() {
        // Configuration du test
        session.setTeacher(null);

        // Exécution du test
        SessionDto resultDto = sessionMapper.toDto(session);

        // Vérifications
        assertNotNull(resultDto);
        assertNull(resultDto.getTeacher_id(), "L'ID du teacher devrait être null quand teacher est null");
    }

    @Test
    public void testToDtoWithNullUsers() {
        // Configuration du test
        session.setUsers(null);

        // Exécution du test
        SessionDto resultDto = sessionMapper.toDto(session);

        // Vérifications
        assertNotNull(resultDto);
        assertNotNull(resultDto.getUsers(), "La liste des utilisateurs ne devrait pas être null");
        assertTrue(resultDto.getUsers().isEmpty(), "La liste des utilisateurs devrait être vide");
    }



    @Test
    public void testToEntityWithNullTeacherId() {
        // Configuration du test
        sessionDto.setTeacher_id(null);

        // Exécution du test
        Session resultSession = sessionMapper.toEntity(sessionDto);

        // Vérifications
        assertNotNull(resultSession);
        assertNull(resultSession.getTeacher(), "Le teacher devrait être null quand teacher_id est null");

        // Vérification qu'aucun appel n'a été fait au teacherService
        verify(teacherService, never()).findById(anyLong());
    }

    @Test
    public void testToEntityWithNullUsers() {
        // Configuration du test
        sessionDto.setUsers(null);

        // Exécution du test
        Session resultSession = sessionMapper.toEntity(sessionDto);

        // Vérifications
        assertNotNull(resultSession);
        assertNotNull(resultSession.getUsers(), "La liste des utilisateurs ne devrait pas être null");
        assertTrue(resultSession.getUsers().isEmpty(), "La liste des utilisateurs devrait être vide");

        // Vérification qu'aucun appel n'a été fait au userService
        verify(userService, never()).findById(anyLong());
    }

    @Test
    public void testToEntityWithNonExistentUser() {
        // Configuration du test
        sessionDto.setUsers(Arrays.asList(1L, 3L)); // 3L n'existe pas

        // Configuration des mocks
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(3L)).thenReturn(null); // L'utilisateur 3 n'existe pas

        // Exécution du test
        Session resultSession = sessionMapper.toEntity(sessionDto);

        // Vérifications
        assertNotNull(resultSession);
        assertNotNull(resultSession.getUsers());
        assertEquals(2, resultSession.getUsers().size()); // L'implémentation garde les utilisateurs null
        assertEquals(user1.getId(), resultSession.getUsers().get(0).getId());
        assertNull(resultSession.getUsers().get(1)); // Le deuxième élément devrait être null

        // Vérification des appels de méthodes
        verify(userService).findById(1L);
        verify(userService).findById(3L);
    }

    @Test
    public void testToEntityList() {
        // Configuration des mocks
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Configuration du test
        List<SessionDto> dtoList = Arrays.asList(sessionDto);

        // Exécution du test
        List<Session> resultList = sessionMapper.toEntity(dtoList);

        // Vérifications
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(sessionDto.getId(), resultList.get(0).getId());
        assertEquals(sessionDto.getName(), resultList.get(0).getName());
    }

    @Test
    public void testToDtoList() {
        // Configuration du test
        List<Session> entityList = Arrays.asList(session);

        // Exécution du test
        List<SessionDto> resultList = sessionMapper.toDto(entityList);

        // Vérifications
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(session.getId(), resultList.get(0).getId());
        assertEquals(session.getName(), resultList.get(0).getName());
    }
}