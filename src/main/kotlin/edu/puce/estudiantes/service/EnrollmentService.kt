package edu.puce.estudiantes.service

import edu.puce.estudiantes.dto.EnrollmentRequest
import edu.puce.estudiantes.dto.EnrollmentResponse
import edu.puce.estudiantes.entity.Enrollment
import edu.puce.estudiantes.repository.EnrollmentRepository
import edu.puce.estudiantes.repository.StudentRepository
import edu.puce.estudiantes.repository.SubjectRepository
import org.springframework.stereotype.Service
import edu.puce.estudiantes.exceptions.EnrollmentNotFoundException
import edu.puce.estudiantes.exceptions.SubjectNotFoundException
import edu.puce.estudiantes.exceptions.StudentNotFoundException

@Service
class EnrollmentService(private val repo: EnrollmentRepository, private val sRepo: StudentRepository, private val subRepo: SubjectRepository, private val mapper: EnrollmentMapper) {
    fun enroll(req: EnrollmentRequest): EnrollmentResponse {
        val s = sRepo.findById(req.studentId).orElseThrow { StudentNotFoundException("Estudiante no encontrado") }
        val sub = subRepo.findById(req.subjectId).orElseThrow { SubjectNotFoundException("Materia no encontrada") }
        return mapper.toResponse(repo.save(Enrollment(student = s, subject = sub)))
    }
    fun getAll() = repo.findAll().map { mapper.toResponse(it) }
    fun getById(id: Long) = mapper.toResponse(repo.findById(id).orElseThrow { EnrollmentNotFoundException("Inscripción no encontrada") })
    fun updateStatus(id: Long, newStatus: String): EnrollmentResponse {
        val e = repo.findById(id).orElseThrow { EnrollmentNotFoundException("Inscripción no encontrada") }
        e.status = newStatus
        return mapper.toResponse(repo.save(e))
    }
    fun delete(id: Long) {
        if (!repo.existsById(id)) throw EnrollmentNotFoundException("Inscripción no encontrada")
        repo.deleteById(id)
    }
}