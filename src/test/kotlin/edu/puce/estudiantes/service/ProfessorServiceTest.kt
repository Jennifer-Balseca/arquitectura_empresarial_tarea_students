package edu.puce.estudiantes.service

import edu.puce.estudiantes.dto.ProfessorRequest
import edu.puce.estudiantes.dto.ProfessorResponse
import edu.puce.estudiantes.entity.Professor
import edu.puce.estudiantes.exceptions.BlankMesaggeException
import edu.puce.estudiantes.exceptions.ProfessorNotFoundException
import edu.puce.estudiantes.mapper.ProfessorMapper
import edu.puce.estudiantes.repositories.ProfessorRepository
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
class ProfessorServiceTest {

    @Mock private lateinit var repository: ProfessorRepository
    @Mock private lateinit var mapper: ProfessorMapper
    @InjectMocks private lateinit var service: ProfessorService

    @Test
    fun `create lanza BlankMesaggeException cuando el nombre esta vacio`() {
        // Arrange
        val req = ProfessorRequest("", "prof@puce.edu")

        // Act
        val act = { service.create(req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }

    @Test
    fun `create retorna ProfessorResponse cuando los datos son validos`() {
        // Arrange
        val req      = ProfessorRequest("Carlos Ruiz", "carlos@puce.edu")
        val entity   = Professor(id = 0L, name = "Carlos Ruiz", email = "carlos@puce.edu")
        val saved    = Professor(id = 1L, name = "Carlos Ruiz", email = "carlos@puce.edu")
        val response = ProfessorResponse(id = 1L, name = "Carlos Ruiz", email = "carlos@puce.edu")

        `when`(mapper.toEntity(req)).thenReturn(entity)
        `when`(repository.save(entity)).thenReturn(saved)
        `when`(mapper.toResponse(saved)).thenReturn(response)

        // Act
        val result = service.create(req)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Carlos Ruiz", result.name)
    }

    @Test
    fun `getAll retorna lista de ProfessorResponse`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Luis", email = "luis@puce.edu")
        val response  = ProfessorResponse(id = 1L, name = "Luis", email = "luis@puce.edu")

        `when`(repository.findAll()).thenReturn(listOf(professor))
        `when`(mapper.toResponse(professor)).thenReturn(response)

        // Act
        val result = service.getAll()

        // Assert
        assertEquals(1, result.size)
        assertEquals("Luis", result[0].name)
    }

    @Test
    fun `getById retorna ProfessorResponse cuando el profesor existe`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Pedro", email = "pedro@puce.edu")
        val response  = ProfessorResponse(id = 1L, name = "Pedro", email = "pedro@puce.edu")

        `when`(repository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(mapper.toResponse(professor)).thenReturn(response)

        // Act
        val result = service.getById(1L)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Pedro", result.name)
    }

    @Test
    fun `getById lanza ProfessorNotFoundException cuando el profesor no existe`() {
        // Arrange
        `when`(repository.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.getById(99L) }

        // Assert
        assertThrows<ProfessorNotFoundException> { act() }
    }

    @Test
    fun `update lanza BlankMesaggeException cuando el nombre esta vacio`() {
        // Arrange
        val req = ProfessorRequest("", "prof@puce.edu")

        // Act
        val act = { service.update(1L, req) }

        // Assert
        assertThrows<BlankMesaggeException> { act() }
    }

    @Test
    fun `update lanza ProfessorNotFoundException cuando el profesor no existe`() {
        // Arrange
        val req = ProfessorRequest("Nombre Valido", "prof@puce.edu")
        `when`(repository.findById(1L)).thenReturn(Optional.empty())

        // Act
        val act = { service.update(1L, req) }

        // Assert
        assertThrows<ProfessorNotFoundException> { act() }
    }

    @Test
    fun `update retorna ProfessorResponse actualizado cuando los datos son validos`() {
        // Arrange
        val req      = ProfessorRequest("Carlos Actualizado", "carlos2@puce.edu")
        val existing = Professor(id = 1L, name = "Carlos Ruiz",       email = "carlos@puce.edu")
        val updated  = Professor(id = 1L, name = "Carlos Actualizado", email = "carlos2@puce.edu")
        val response = ProfessorResponse(id = 1L, name = "Carlos Actualizado", email = "carlos2@puce.edu")

        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(repository.save(any(Professor::class.java))).thenReturn(updated)
        `when`(mapper.toResponse(updated)).thenReturn(response)

        // Act
        val result = service.update(1L, req)

        // Assert
        assertEquals("Carlos Actualizado", result.name)
    }

    @Test
    fun `delete lanza ProfessorNotFoundException cuando el profesor no existe`() {
        // Arrange
        `when`(repository.existsById(99L)).thenReturn(false)

        // Act
        val act = { service.delete(99L) }

        // Assert
        assertThrows<ProfessorNotFoundException> { act() }
    }

    @Test
    fun `delete elimina el profesor cuando existe`() {
        // Arrange
        `when`(repository.existsById(1L)).thenReturn(true)

        // Act
        service.delete(1L)

        // Assert
        verify(repository).deleteById(1L)
    }
}