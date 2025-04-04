package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void testToDto() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        // Act
        TeacherDto result = teacherMapper.toDto(teacher);

        // Assert
        assertNotNull(result);
        assertEquals(teacher.getId(), result.getId());
        assertEquals(teacher.getFirstName(), result.getFirstName());
        assertEquals(teacher.getLastName(), result.getLastName());
        assertEquals(teacher.getCreatedAt(), result.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    public void testToEntity() {
        // Arrange
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        LocalDateTime now = LocalDateTime.now();
        teacherDto.setCreatedAt(now);
        teacherDto.setUpdatedAt(now);

        // Act
        Teacher result = teacherMapper.toEntity(teacherDto);

        // Assert
        assertNotNull(result);
        assertEquals(teacherDto.getId(), result.getId());
        assertEquals(teacherDto.getFirstName(), result.getFirstName());
        assertEquals(teacherDto.getLastName(), result.getLastName());
        assertEquals(teacherDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    public void testToDtoList() {
        // Arrange
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        // Act
        List<TeacherDto> result = teacherMapper.toDto(teachers);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(teacher1.getId(), result.get(0).getId());
        assertEquals(teacher1.getFirstName(), result.get(0).getFirstName());
        assertEquals(teacher1.getLastName(), result.get(0).getLastName());
        assertEquals(teacher2.getId(), result.get(1).getId());
        assertEquals(teacher2.getFirstName(), result.get(1).getFirstName());
        assertEquals(teacher2.getLastName(), result.get(1).getLastName());
    }

    @Test
    public void testToEntityList() {
        // Arrange
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");

        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        // Act
        List<Teacher> result = teacherMapper.toEntity(teacherDtos);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(teacherDto1.getId(), result.get(0).getId());
        assertEquals(teacherDto1.getFirstName(), result.get(0).getFirstName());
        assertEquals(teacherDto1.getLastName(), result.get(0).getLastName());
        assertEquals(teacherDto2.getId(), result.get(1).getId());
        assertEquals(teacherDto2.getFirstName(), result.get(1).getFirstName());
        assertEquals(teacherDto2.getLastName(), result.get(1).getLastName());
    }

    @Test
    public void testNullToDto() {
        // Act
        TeacherDto result = teacherMapper.toDto((Teacher) null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testNullToEntity() {
        // Act
        Teacher result = teacherMapper.toEntity((TeacherDto) null);

        // Assert
        assertNull(result);
    }
}