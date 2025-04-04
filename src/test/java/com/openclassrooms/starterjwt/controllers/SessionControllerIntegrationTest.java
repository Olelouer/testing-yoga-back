package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private Session session1, session2;
    private SessionDto sessionDto1, sessionDto2;
    private String jwtToken;
    private User testUser;
    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        // Créer un utilisateur de test pour l'authentification
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPassword("password");
        testUser.setAdmin(false);

        // Créer un enseignant pour les sessions
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Teacher");

        // Créer les sessions de test
        session1 = new Session();
        session1.setId(1L);
        session1.setName("Yoga Session 1");
        session1.setDate(new Date());
        session1.setDescription("A relaxing yoga session");
        session1.setTeacher(teacher);
        session1.setUsers(new ArrayList<>(Arrays.asList(testUser)));

        session2 = new Session();
        session2.setId(2L);
        session2.setName("Yoga Session 2");
        session2.setDate(new Date());
        session2.setDescription("An intense yoga session");
        session2.setTeacher(teacher);
        session2.setUsers(new ArrayList<>());

        // Créer les DTOs correspondants
        sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Yoga Session 1");
        sessionDto1.setDate(new Date());
        sessionDto1.setDescription("A relaxing yoga session");
        sessionDto1.setTeacher_id(1L);
        sessionDto1.setUsers(Arrays.asList(1L));

        sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Yoga Session 2");
        sessionDto2.setDate(new Date());
        sessionDto2.setDescription("An intense yoga session");
        sessionDto2.setTeacher_id(1L);
        sessionDto2.setUsers(Arrays.asList());

        // Authentifier l'utilisateur
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(testUser.getId())
                .username(testUser.getEmail())
                .firstName(testUser.getFirstName())
                .lastName(testUser.getLastName())
                .password(testUser.getPassword())
                .admin(testUser.isAdmin())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Générer un token JWT
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testFindByIdSuccess() throws Exception {
        // Configuration des mocks
        when(sessionService.getById(1L)).thenReturn(session1);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto1);

        // Exécution du test et vérification
        mockMvc.perform(get("/api/session/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionDto1.getId()))
                .andExpect(jsonPath("$.name").value(sessionDto1.getName()))
                .andExpect(jsonPath("$.description").value(sessionDto1.getDescription()));

        // Vérification des appels aux services
        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(any(Session.class));
    }

    @Test
    public void testFindByIdSessionNotFound() throws Exception {
        // Configuration des mocks
        when(sessionService.getById(99L)).thenReturn(null);

        // Exécution du test et vérification
        mockMvc.perform(get("/api/session/99")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Vérification des appels aux services
        verify(sessionService).getById(99L);
        verify(sessionMapper, never()).toDto(any(Session.class));
    }

    @Test
    public void testFindByIdInvalidId() throws Exception {
        // Exécution du test et vérification
        mockMvc.perform(get("/api/session/invalid")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Vérification qu'aucun appel aux services n'a été fait
        verify(sessionService, never()).getById(anyLong());
        verify(sessionMapper, never()).toDto(any(Session.class));
    }

    @Test
    public void testCreateSuccess() throws Exception {
        // Configuration des mocks
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session1);
        when(sessionService.create(any(Session.class))).thenReturn(session1);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto1);

        // Exécution du test et vérification
        mockMvc.perform(post("/api/session")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionDto1.getId()))
                .andExpect(jsonPath("$.name").value(sessionDto1.getName()));

        // Vérification des appels aux services
        verify(sessionMapper).toEntity(any(SessionDto.class));
        verify(sessionService).create(any(Session.class));
        verify(sessionMapper).toDto(any(Session.class));
    }

    @Test
    public void testUpdateSuccess() throws Exception {
        // Configuration des mocks
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session1);
        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(session1);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto1);

        // Exécution du test et vérification
        mockMvc.perform(put("/api/session/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionDto1.getId()))
                .andExpect(jsonPath("$.name").value(sessionDto1.getName()));

        // Vérification des appels aux services
        verify(sessionMapper).toEntity(any(SessionDto.class));
        verify(sessionService).update(eq(1L), any(Session.class));
        verify(sessionMapper).toDto(any(Session.class));
    }

    @Test
    public void testUpdateInvalidId() throws Exception {
        // Exécution du test et vérification
        mockMvc.perform(put("/api/session/invalid")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto1)))
                .andExpect(status().isBadRequest());

        // Vérification qu'aucun appel aux services n'a été fait
        verify(sessionMapper, never()).toEntity(any(SessionDto.class));
        verify(sessionService, never()).update(anyLong(), any(Session.class));
        verify(sessionMapper, never()).toDto(any(Session.class));
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        // Configuration des mocks
        when(sessionService.getById(1L)).thenReturn(session1);
        doNothing().when(sessionService).delete(1L);

        // Exécution du test et vérification
        mockMvc.perform(delete("/api/session/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Vérification des appels aux services
        verify(sessionService).getById(1L);
        verify(sessionService).delete(1L);
    }

    @Test
    public void testDeleteSessionNotFound() throws Exception {
        // Configuration des mocks
        when(sessionService.getById(99L)).thenReturn(null);

        // Exécution du test et vérification
        mockMvc.perform(delete("/api/session/99")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Vérification des appels aux services
        verify(sessionService).getById(99L);
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteInvalidId() throws Exception {
        // Exécution du test et vérification
        mockMvc.perform(delete("/api/session/invalid")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Vérification qu'aucun appel aux services n'a été fait
        verify(sessionService, never()).getById(anyLong());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    public void testParticipateSuccess() throws Exception {
        // Configuration des mocks
        doNothing().when(sessionService).participate(1L, 2L);

        // Exécution du test et vérification
        mockMvc.perform(post("/api/session/1/participate/2")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Vérification des appels aux services
        verify(sessionService).participate(1L, 2L);
    }

    @Test
    public void testParticipateInvalidIds() throws Exception {
        // Exécution du test et vérification
        mockMvc.perform(post("/api/session/invalid/participate/2")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Vérification qu'aucun appel aux services n'a été fait
        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @Test
    public void testParticipateNotFound() throws Exception {
        // Configuration des mocks
        doThrow(new NotFoundException()).when(sessionService).participate(99L, 2L);

        // Exécution du test et vérification
        mockMvc.perform(post("/api/session/99/participate/2")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Vérification des appels aux services
        verify(sessionService).participate(99L, 2L);
    }

    @Test
    public void testNoLongerParticipateSuccess() throws Exception {
        // Configuration des mocks
        doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        // Exécution du test et vérification
        mockMvc.perform(delete("/api/session/1/participate/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Vérification des appels aux services
        verify(sessionService).noLongerParticipate(1L, 1L);
    }

    @Test
    public void testNoLongerParticipateInvalidIds() throws Exception {
        // Exécution du test et vérification
        mockMvc.perform(delete("/api/session/invalid/participate/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Vérification qu'aucun appel aux services n'a été fait
        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }

    @Test
    public void testNoLongerParticipateNotFound() throws Exception {
        // Configuration des mocks
        doThrow(new NotFoundException()).when(sessionService).noLongerParticipate(99L, 1L);

        // Exécution du test et vérification
        mockMvc.perform(delete("/api/session/99/participate/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Vérification des appels aux services
        verify(sessionService).noLongerParticipate(99L, 1L);
    }
}