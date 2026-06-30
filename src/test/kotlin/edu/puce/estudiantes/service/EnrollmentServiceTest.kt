package edu.puce.estudiantes.service

import edu.puce.estudiantes.dto.EnrollmentRequest
import edu.puce.estudiantes.dto.EnrollmentResponse
import edu.puce.estudiantes.dto.ProfessorResponse
import edu.puce.estudiantes.dto.StudentResponse
import edu.puce.estudiantes.dto.SubjectResponse
import edu.puce.estudiantes.entity.Enrollment
import edu.puce.estudiantes.entity.Professor
import edu.puce.estudiantes.entity.Student
import edu.puce.estudiantes.entity.Subject
import edu.puce.estudiantes.exceptions.EnrollmentNotFoundException
import edu.puce.estudiantes.exceptions.StudentNotFoundException
import edu.puce.estudiantes.exceptions.SubjectNotFoundException
import edu.puce.estudiantes.mapper.EnrollmentMapper
import edu.puce.estudiantes.repositories.EnrollmentRepository
import edu.puce.estudiantes.repositories.StudentRepository
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
class EnrollmentServiceTest {

    @Mock private lateinit var repo: EnrollmentRepository
    @Mock private lateinit var sRepo: StudentRepository
    @Mock private lateinit var subRepo: SubjectRepository
    @Mock private lateinit var mapper: EnrollmentMapper
    @InjectMocks private lateinit var service: EnrollmentService

    private val professor   = Professor(id = 1L, name = "Luis", email = "luis@puce.edu")
    private val student     = Student(id = 1L, name = "Ana", email = "ana@puce.edu")
    private val subject     = Subject(id = 1L, name = "Matematicas", code = "M01", professor = professor)
    private val enrollment  = Enrollment(id = 1L, student = student, subject = subject, status = "INSCRITO")

    private val studentResp = StudentResponse(id = 1L, name = "Ana", email = "ana@puce.edu")
    private val profResp    = ProfessorResponse(id = 1L, name = "Luis", email = "luis@puce.edu")
    private val subjectResp = SubjectResponse(id = 1L, name = "Matematicas", code = "M01", professor = profResp)
    private val enrollResp  = EnrollmentResponse(
        id = 1L, createdAt = enrollment.createdAt,
        status = "INSCRITO", student = studentResp, subject = subjectResp
    )

    @Test
    fun `enroll lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        val req = EnrollmentRequest(studentId = 99L, subjectId = 1L)
        `when`(sRepo.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.enroll(req) }

        // Assert
        assertThrows<StudentNotFoundException> { act() }
    }

    @Test
    fun `enroll lanza SubjectNotFoundException cuando la materia no existe`() {
        // Arrange
        val req = EnrollmentRequest(studentId = 1L, subjectId = 99L)
        `when`(sRepo.findById(1L)).thenReturn(Optional.of(student))
        `when`(subRepo.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.enroll(req) }

        // Assert
        assertThrows<SubjectNotFoundException> { act() }
    }

    @Test
    fun `enroll retorna EnrollmentResponse cuando los datos son validos`() {
        // Arrange
        val req = EnrollmentRequest(studentId = 1L, subjectId = 1L)
        `when`(sRepo.findById(1L)).thenReturn(Optional.of(student))
        `when`(subRepo.findById(1L)).thenReturn(Optional.of(subject))
        `when`(repo.save(any(Enrollment::class.java))).thenReturn(enrollment)
        `when`(mapper.toResponse(enrollment)).thenReturn(enrollResp)

        // Act
        val result = service.enroll(req)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("INSCRITO", result.status)
    }

    @Test
    fun `getAll retorna lista de EnrollmentResponse`() {
        // Arrange
        `when`(repo.findAll()).thenReturn(listOf(enrollment))
        `when`(mapper.toResponse(enrollment)).thenReturn(enrollResp)

        // Act
        val result = service.getAll()

        // Assert
        assertEquals(1, result.size)
        assertEquals(1L, result[0].id)
    }

    @Test
    fun `getById retorna EnrollmentResponse cuando la inscripcion existe`() {
        // Arrange
        `when`(repo.findById(1L)).thenReturn(Optional.of(enrollment))
        `when`(mapper.toResponse(enrollment)).thenReturn(enrollResp)

        // Act
        val result = service.getById(1L)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("INSCRITO", result.status)
    }

    @Test
    fun `getById lanza EnrollmentNotFoundException cuando la inscripcion no existe`() {
        // Arrange
        `when`(repo.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.getById(99L) }

        // Assert
        assertThrows<EnrollmentNotFoundException> { act() }
    }

    @Test
    fun `updateStatus lanza EnrollmentNotFoundException cuando la inscripcion no existe`() {
        // Arrange
        `when`(repo.findById(99L)).thenReturn(Optional.empty())

        // Act
        val act = { service.updateStatus(99L, "APROBADO") }

        // Assert
        assertThrows<EnrollmentNotFoundException> { act() }
    }

    @Test
    fun `updateStatus actualiza el estado y retorna EnrollmentResponse`() {
        // Arrange
        val updatedEnrollment = Enrollment(id = 1L, student = student, subject = subject, status = "APROBADO")
        val updatedResp = enrollResp.copy(status = "APROBADO")
        `when`(repo.findById(1L)).thenReturn(Optional.of(enrollment))
        `when`(repo.save(enrollment)).thenReturn(updatedEnrollment)
        `when`(mapper.toResponse(updatedEnrollment)).thenReturn(updatedResp)

        // Act
        val result = service.updateStatus(1L, "APROBADO")

        // Assert
        assertEquals("APROBADO", result.status)
    }

    @Test
    fun `delete lanza EnrollmentNotFoundException cuando la inscripcion no existe`() {
        // Arrange
        `when`(repo.existsById(99L)).thenReturn(false)

        // Act
        val act = { service.delete(99L) }

        // Assert
        assertThrows<EnrollmentNotFoundException> { act() }
    }

    @Test
    fun `delete elimina la inscripcion cuando existe`() {
        // Arrange
        `when`(repo.existsById(1L)).thenReturn(true)

        // Act
        service.delete(1L)

        // Assert
        verify(repo).deleteById(1L)
    }
}