package edu.puce.estudiantes.service

import edu.puce.estudiantes.dto.ProfessorResponse
import edu.puce.estudiantes.dto.SubjectRequest
import edu.puce.estudiantes.dto.SubjectResponse
import edu.puce.estudiantes.entity.Professor
import edu.puce.estudiantes.entity.Subject
import edu.puce.estudiantes.exceptions.BlankMesaggeException
import edu.puce.estudiantes.exceptions.ProfessorNotFoundException
import edu.puce.estudiantes.exceptions.SubjectNotFoundException
import edu.puce.estudiantes.mapper.SubjectMapper
import edu.puce.estudiantes.repositories.ProfessorRepository
import edu.puce.estudiantes.repositories.SubjectRepository
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
class SubjectServiceTest {

    @Mock private lateinit var repository: SubjectRepository
    @Mock private lateinit var profRepository: ProfessorRepository
    @Mock private lateinit var mapper: SubjectMapper
    @InjectMocks private lateinit var service: SubjectService

    private val professor = Professor(id = 1L, name = "Luis", email = "luis@puce.edu")
    private val profResponse = ProfessorResponse(id = 1L, name = "Luis", email = "luis@puce.edu")

    @Test
    fun `create lanza BlankMesaggeException cuando el nombre esta vacio`() {
        // Arrange
        val req = SubjectRequest("", "M01", 1L)

        // Act
        val act = { service.create(req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }

    @Test
    fun `create lanza BlankMesaggeException cuando el codigo esta vacio`() {
        // Arrange
        val req = SubjectRequest("Matematicas", "", 1L)

        // Act
        val act = { service.create(req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }

    @Test
    fun `create lanza ProfessorNotFoundException cuando el profesor no existe`() {
        // Arrange
        val req = SubjectRequest("Matematicas", "M01", 99L)
        `when`(profRepository.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.create(req) }

        // Assert
        assertThrows<ProfessorNotFoundException> { act() }
    }

    @Test
    fun `create retorna SubjectResponse cuando los datos son validos`() {
        // Arrange
        val req      = SubjectRequest("Matematicas", "M01", 1L)
        val entity   = Subject(id = 0L, name = "Matematicas", code = "M01", professor = professor)
        val saved    = Subject(id = 1L, name = "Matematicas", code = "M01", professor = professor)
        val response = SubjectResponse(id = 1L, name = "Matematicas", code = "M01", professor = profResponse)

        `when`(profRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(mapper.toEntity(req, professor)).thenReturn(entity)
        `when`(repository.save(entity)).thenReturn(saved)
        `when`(mapper.toResponse(saved)).thenReturn(response)

        // Act
        val result = service.create(req)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Matematicas", result.name)
    }

    @Test
    fun `getAll retorna lista de SubjectResponse`() {
        // Arrange
        val subject  = Subject(id = 1L, name = "Fisica", code = "F01", professor = professor)
        val response = SubjectResponse(id = 1L, name = "Fisica", code = "F01", professor = profResponse)

        `when`(repository.findAll()).thenReturn(listOf(subject))
        `when`(mapper.toResponse(subject)).thenReturn(response)

        // Act
        val result = service.getAll()

        // Assert
        assertEquals(1, result.size)
        assertEquals("Fisica", result[0].name)
    }

    @Test
    fun `getById retorna SubjectResponse cuando la materia existe`() {
        // Arrange
        val subject  = Subject(id = 1L, name = "Quimica", code = "Q01", professor = professor)
        val response = SubjectResponse(id = 1L, name = "Quimica", code = "Q01", professor = profResponse)

        `when`(repository.findById(1L)).thenReturn(Optional.of(subject))
        `when`(mapper.toResponse(subject)).thenReturn(response)

        // Act
        val result = service.getById(1L)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Quimica", result.name)
    }

    @Test
    fun `getById lanza SubjectNotFoundException cuando la materia no existe`() {
        // Arrange
        `when`(repository.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.getById(99L) }

        // Assert
        assertThrows<SubjectNotFoundException> { act() }
    }

    @Test
    fun `update lanza BlankMesaggeException cuando el nombre esta vacio`() {
        // Arrange
        val req = SubjectRequest("", "M01", 1L)

        // Act
        val act = { service.update(1L, req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }

    @Test
    fun `update lanza BlankMesaggeException cuando el codigo esta vacio`() {
        // Arrange
        val req = SubjectRequest("Matematicas", "", 1L)

        // Act
        val act = { service.update(1L, req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }

    @Test
    fun `update lanza SubjectNotFoundException cuando la materia no existe`() {
        // Arrange
        val req = SubjectRequest("Matematicas", "M01", 1L)
        `when`(repository.findById(1L)).thenReturn(Optional.empty())

        // Act
        val act = { service.update(1L, req) }

        // Assert
        assertThrows<SubjectNotFoundException> { act() }
    }

    @Test
    fun `update lanza ProfessorNotFoundException cuando el profesor no existe`() {
        // Arrange
        val req      = SubjectRequest("Matematicas", "M01", 99L)
        val existing = Subject(id = 1L, name = "Matematicas", code = "M01", professor = professor)

        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(profRepository.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.update(1L, req) }

        // Assert
        assertThrows<ProfessorNotFoundException> { act() }
    }

    @Test
    fun `update retorna SubjectResponse actualizado cuando los datos son validos`() {
        // Arrange
        val req      = SubjectRequest("Matematicas II", "M02", 1L)
        val existing = Subject(id = 1L, name = "Matematicas",    code = "M01", professor = professor)
        val updated  = Subject(id = 1L, name = "Matematicas II", code = "M02", professor = professor)
        val response = SubjectResponse(id = 1L, name = "Matematicas II", code = "M02", professor = profResponse)

        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(profRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(repository.save(any(Subject::class.java))).thenReturn(updated)
        `when`(mapper.toResponse(updated)).thenReturn(response)

        // Act
        val result = service.update(1L, req)

        // Assert
        assertEquals("Matematicas II", result.name)
        assertEquals("M02", result.code)
    }

    @Test
    fun `delete lanza SubjectNotFoundException cuando la materia no existe`() {
        // Arrange
        `when`(repository.existsById(99L)).thenReturn(false)

        // Act
        val act = { service.delete(99L) }

        // Assert
        assertThrows<SubjectNotFoundException> { act() }
    }

    @Test
    fun `delete elimina la materia cuando existe`() {
        // Arrange
        `when`(repository.existsById(1L)).thenReturn(true)

        // Act
        service.delete(1L)

        // Assert
        verify(repository).deleteById(1L)
    }

    @Test
    fun `create lanza BlankMesaggeException cuando nombre y codigo estan vacios`() {
        // Arrange
        val req = SubjectRequest("", "", 1L)

        // Act
        val act = { service.create(req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }
}