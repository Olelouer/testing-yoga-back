package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private User testUser;
    private UserDto testUserDto;
    private String jwtToken;

    @BeforeEach
    public void setUp() {
        // Créer un utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPassword("password");
        testUser.setAdmin(false);

        // Créer un DTO utilisateur
        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@example.com");
        testUserDto.setFirstName("Test");
        testUserDto.setLastName("User");
        testUserDto.setAdmin(false);

        // Créer un UserDetailsImpl en utilisant le builder pattern
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(testUser.getId())
                .username(testUser.getEmail())
                .firstName(testUser.getFirstName())
                .lastName(testUser.getLastName())
                .password(testUser.getPassword())
                .admin(testUser.isAdmin())
                .build();

        // Simuler l'authentification de l'utilisateur
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Générer un token JWT pour l'utilisateur authentifié
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testFindByIdSuccess() throws Exception {
        // Configuration des mocks
        when(userService.findById(1L)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Exécution du test et vérification
        mockMvc.perform(get("/api/user/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testUser.getLastName()));

        // Vérification des appels aux services
        verify(userService).findById(1L);
        verify(userMapper).toDto(testUser);
    }

    @Test
    public void testFindByIdUserNotFound() throws Exception {
        // Configuration des mocks
        when(userService.findById(99L)).thenReturn(null);

        // Exécution du test et vérification
        mockMvc.perform(get("/api/user/99")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Vérification des appels aux services
        verify(userService).findById(99L);
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    public void testFindByIdInvalidId() throws Exception {
        // Exécution du test et vérification
        mockMvc.perform(get("/api/user/invalid")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Vérification qu'aucun appel aux services n'a été fait
        verify(userService, never()).findById(any());
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        // Configuration des mocks
        when(userService.findById(1L)).thenReturn(testUser);
        doNothing().when(userService).delete(1L);

        // Exécution du test et vérification
        mockMvc.perform(delete("/api/user/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Vérification des appels aux services
        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        // Configuration des mocks
        when(userService.findById(99L)).thenReturn(null);

        // Exécution du test et vérification
        mockMvc.perform(delete("/api/user/99")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Vérification des appels aux services
        verify(userService).findById(99L);
        verify(userService, never()).delete(any());
    }

    @Test
    public void testDeleteUserInvalidId() throws Exception {
        // Exécution du test et vérification
        mockMvc.perform(delete("/api/user/invalid")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Vérification qu'aucun appel aux services n'a été fait
        verify(userService, never()).findById(any());
        verify(userService, never()).delete(any());
    }

    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        // Créer un autre utilisateur (pas celui qui est authentifié)
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setEmail("another@example.com");
        anotherUser.setFirstName("Another");
        anotherUser.setLastName("User");

        // Configuration des mocks
        when(userService.findById(2L)).thenReturn(anotherUser);

        // Exécution du test et vérification
        mockMvc.perform(delete("/api/user/2")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // Vérification des appels aux services
        verify(userService).findById(2L);
        verify(userService, never()).delete(any());
    }
}