package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private Teacher teacher1, teacher2;
    private TeacherDto teacherDto1, teacherDto2;
    private String jwtToken;
    private User testUser;

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

        // Créer les enseignants de test
        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        // Créer les DTOs correspondants
        teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");

        teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");

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
        when(teacherService.findById(1L)).thenReturn(teacher1);
        when(teacherMapper.toDto(any(Teacher.class))).thenReturn(teacherDto1);

        // Exécution du test et vérification
        mockMvc.perform(get("/api/teacher/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacherDto1.getId()))
                .andExpect(jsonPath("$.firstName").value(teacherDto1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(teacherDto1.getLastName()));

        // Vérification des appels aux services
        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(any(Teacher.class));
    }

    @Test
    public void testFindByIdTeacherNotFound() throws Exception {
        // Configuration des mocks
        when(teacherService.findById(99L)).thenReturn(null);

        // Exécution du test et vérification
        mockMvc.perform(get("/api/teacher/99")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Vérification des appels aux services
        verify(teacherService).findById(99L);
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    @Test
    public void testFindByIdInvalidId() throws Exception {
        // Exécution du test et vérification
        mockMvc.perform(get("/api/teacher/invalid")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Vérification qu'aucun appel aux services n'a été fait
        verify(teacherService, never()).findById(any());
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    @Test
    public void testFindAllSuccess() throws Exception {
        // Créer une liste d'enseignants
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        // Configuration des mocks
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(anyList())).thenReturn(teacherDtos);

        // Exécution du test et vérification
        mockMvc.perform(get("/api/teacher")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(teacherDto1.getId()))
                .andExpect(jsonPath("$[0].firstName").value(teacherDto1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(teacherDto1.getLastName()))
                .andExpect(jsonPath("$[1].id").value(teacherDto2.getId()))
                .andExpect(jsonPath("$[1].firstName").value(teacherDto2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(teacherDto2.getLastName()));

        // Vérification des appels aux services
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(anyList());
    }
}