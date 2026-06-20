package edu.puce.estudiantes.service

import edu.puce.estudiantes.Mappers.StudentMapper
import edu.puce.estudiantes.Mappers.SubjectMapper
import edu.puce.estudiantes.dto.EnrollmentResponse
import edu.puce.estudiantes.entity.Enrollment
import org.springframework.stereotype.Component

@Component
class EnrollmentMapper(
    private val studentMapper: StudentMapper,
    private val subjectMapper: SubjectMapper
) {
    fun toResponse(e: Enrollment) = EnrollmentResponse(
        id = e.id,
        status = e.status,
        createdAt = e.createdAt,
        student = studentMapper.toResponse(e.student),
        subject = subjectMapper.toResponse(e.subject)
    )
}