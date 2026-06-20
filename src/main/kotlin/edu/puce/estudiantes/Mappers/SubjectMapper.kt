package edu.puce.estudiantes.Mappers

import edu.puce.estudiantes.dto.SubjectRequest
import edu.puce.estudiantes.dto.SubjectResponse
import edu.puce.estudiantes.entity.Professor
import edu.puce.estudiantes.entity.Subject
import org.springframework.stereotype.Component

@Component
class SubjectMapper(private val professorMapper: ProfessorMapper) {

    fun toEntity(req: SubjectRequest, prof: Professor) = Subject(
        name = req.name,
        code = req.code,
        professor = prof
    )

    fun toResponse(sub: Subject) = SubjectResponse(
        sub.id,
        sub.name,
        sub.code,
        professorMapper.toResponse(sub.professor)
    )
}