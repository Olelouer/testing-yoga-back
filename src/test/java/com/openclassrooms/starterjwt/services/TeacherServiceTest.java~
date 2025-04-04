package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    public void setUp() {
        // Initialisation des objets de test
        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(teachers);

        // Act
        List<Teacher> result = teacherService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(teacher1.getId(), result.get(0).getId());
        assertEquals(teacher1.getFirstName(), result.get(0).getFirstName());
        assertEquals(teacher1.getLastName(), result.get(0).getLastName());
        assertEquals(teacher2.getId(), result.get(1).getId());
        assertEquals(teacher2.getFirstName(), result.get(1).getFirstName());
        assertEquals(teacher2.getLastName(), result.get(1).getLastName());
        verify(teacherRepository).findAll();
    }

    @Test
    public void testFindAllEmptyList() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Teacher> result = teacherService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(teacherRepository).findAll();
    }

    @Test
    public void testFindByIdExisting() {
        // Arrange
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher1));

        // Act
        Teacher result = teacherService.findById(teacherId);

        // Assert
        assertNotNull(result);
        assertEquals(teacherId, result.getId());
        assertEquals(teacher1.getFirstName(), result.getFirstName());
        assertEquals(teacher1.getLastName(), result.getLastName());
        verify(teacherRepository).findById(teacherId);
    }

    @Test
    public void testFindByIdNonExisting() {
        // Arrange
        Long teacherId = 99L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Act
        Teacher result = teacherService.findById(teacherId);

        // Assert
        assertNull(result);
        verify(teacherRepository).findById(teacherId);
    }
}