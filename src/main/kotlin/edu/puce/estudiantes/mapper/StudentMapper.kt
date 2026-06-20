package edu.puce.estudiantes.mapper

import edu.puce.estudiantes.dto.StudentRequest
import edu.puce.estudiantes.dto.StudentResponse
import edu.puce.estudiantes.entity.Student
import org.springframework.stereotype.Component


@Component
class StudentMapper {
    fun toEntity(req: StudentRequest) = Student(name = req.name, email = req.email)

    fun toResponse(s: Student) = StudentResponse(
        id = s.id,
        name = s.name,
        email = s.email
    )
}