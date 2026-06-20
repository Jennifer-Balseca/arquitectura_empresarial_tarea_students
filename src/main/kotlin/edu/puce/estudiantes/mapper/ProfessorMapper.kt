package edu.puce.estudiantes.mapper

import edu.puce.estudiantes.dto.ProfessorRequest
import edu.puce.estudiantes.dto.ProfessorResponse
import edu.puce.estudiantes.entity.Professor
import org.springframework.stereotype.Component

@Component
class ProfessorMapper {
    fun toEntity(req: ProfessorRequest) = Professor(name = req.name, email = req.email)

    fun toResponse(prof: Professor) = ProfessorResponse(
        id = prof.id,
        name = prof.name,
        email = prof.email
    )
}