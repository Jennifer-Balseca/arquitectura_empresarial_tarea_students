package edu.puce.estudiantes.service

import edu.puce.estudiantes.dto.StudentRequest
import edu.puce.estudiantes.dto.StudentResponse
import edu.puce.estudiantes.entity.Student
import edu.puce.estudiantes.exceptions.BlankMesaggeException
import edu.puce.estudiantes.exceptions.StudentNotFoundException
import edu.puce.estudiantes.mapper.StudentMapper
import edu.puce.estudiantes.repositories.StudentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

    @Mock private lateinit var repository: StudentRepository
    @Mock private lateinit var mapper: StudentMapper
    @InjectMocks private lateinit var service: StudentService

    @Test
    fun `create lanza BlankMesaggeException cuando el nombre esta vacio`() {
        // Arrange
        val req = StudentRequest("", "test@test.com")

        // Act
        val act = { service.create(req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }

    @Test
    fun `create retorna StudentResponse cuando los datos son validos`() {
        // Arrange
        val req = StudentRequest("Ana Lopez", "ana@puce.edu")
        val entity = Student(id = 0L, name = "Ana Lopez", email = "ana@puce.edu")
        val saved  = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val response = StudentResponse(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")

        `when`(mapper.toEntity(req)).thenReturn(entity)
        `when`(repository.save(entity)).thenReturn(saved)
        `when`(mapper.toResponse(saved)).thenReturn(response)

        // Act
        val result = service.create(req)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Ana Lopez", result.name)
    }

    @Test
    fun `getAll retorna lista de StudentResponse`() {
        // Arrange
        val student = Student(id = 1L, name = "Juan", email = "juan@puce.edu")
        val response = StudentResponse(id = 1L, name = "Juan", email = "juan@puce.edu")

        `when`(repository.findAll()).thenReturn(listOf(student))
        `when`(mapper.toResponse(student)).thenReturn(response)

        // Act
        val result = service.getAll()

        // Assert
        assertEquals(1, result.size)
        assertEquals("Juan", result[0].name)
    }

    @Test
    fun `getById retorna StudentResponse cuando el estudiante existe`() {
        // Arrange
        val student = Student(id = 1L, name = "Maria", email = "maria@puce.edu")
        val response = StudentResponse(id = 1L, name = "Maria", email = "maria@puce.edu")

        `when`(repository.findById(1L)).thenReturn(Optional.of(student))
        `when`(mapper.toResponse(student)).thenReturn(response)

        // Act
        val result = service.getById(1L)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Maria", result.name)
    }

    @Test
    fun `getById lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        `when`(repository.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.getById(99L) }

        // Assert
        assertThrows<StudentNotFoundException> { act() }
    }

    @Test
    fun `update lanza BlankMesaggeException cuando el nombre esta vacio`() {
        // Arrange
        val req = StudentRequest("", "test@test.com")

        // Act
        val act = { service.update(1L, req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }

    @Test
    fun `update lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        val req = StudentRequest("Nombre Valido", "test@test.com")
        `when`(repository.findById(1L)).thenReturn(Optional.empty())

        // Act
        val act = { service.update(1L, req) }

        // Assert
        assertThrows<StudentNotFoundException> { act() }
    }

    @Test
    fun `update retorna StudentResponse actualizado cuando los datos son validos`() {
        // Arrange
        val req      = StudentRequest("Ana Actualizada", "ana2@puce.edu")
        val existing = Student(id = 1L, name = "Ana Lopez",       email = "ana@puce.edu")
        val updated  = Student(id = 1L, name = "Ana Actualizada", email = "ana2@puce.edu")
        val response = StudentResponse(id = 1L, name = "Ana Actualizada", email = "ana2@puce.edu")

        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(repository.save(any(Student::class.java))).thenReturn(updated)
        `when`(mapper.toResponse(updated)).thenReturn(response)

        // Act
        val result = service.update(1L, req)

        // Assert
        assertEquals("Ana Actualizada", result.name)
    }

    @Test
    fun `delete lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        `when`(repository.existsById(99L)).thenReturn(false)

        // Act
        val act = { service.delete(99L) }

        // Assert
        assertThrows<StudentNotFoundException> { act() }
    }

    @Test
    fun `delete elimina el estudiante cuando existe`() {
        // Arrange
        `when`(repository.existsById(1L)).thenReturn(true)

        // Act
        service.delete(1L)

        // Assert
        verify(repository).deleteById(1L)
    }
}