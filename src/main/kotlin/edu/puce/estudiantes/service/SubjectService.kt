package edu.puce.estudiantes.service

import edu.puce.estudiantes.mapper.SubjectMapper
import edu.puce.estudiantes.dto.SubjectRequest
import edu.puce.estudiantes.dto.SubjectResponse
import edu.puce.estudiantes.entity.Subject
import edu.puce.estudiantes.exceptions.BlankMesaggeException
import edu.puce.estudiantes.repositories.ProfessorRepository
import edu.puce.estudiantes.repositories.SubjectRepository
import org.springframework.stereotype.Service
import edu.puce.estudiantes.exceptions.SubjectNotFoundException
import edu.puce.estudiantes.exceptions.ProfessorNotFoundException

@Service
class SubjectService(private val repository: SubjectRepository, private val profRepository: ProfessorRepository, private val mapper: SubjectMapper) {
    fun create(req: SubjectRequest): SubjectResponse {
        if (req.name.isBlank() || req.code.isBlank()) throw BlankMesaggeException("El nombre y código no pueden estar vacíos")
        val prof = profRepository.findById(req.professorId).orElseThrow { ProfessorNotFoundException("Profesor no encontrado") }
        return mapper.toResponse(repository.save(mapper.toEntity(req, prof)))
    }
    fun getAll() = repository.findAll().map { mapper.toResponse(it) }
    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { SubjectNotFoundException("Materia no encontrada") })
    fun update(id: Long, req: SubjectRequest): SubjectResponse {
        if (req.name.isBlank() || req.code.isBlank()) throw BlankMesaggeException("El nombre y código no pueden estar vacíos")
        val existing = repository.findById(id).orElseThrow { SubjectNotFoundException("Materia no encontrada") }
        val prof = profRepository.findById(req.professorId).orElseThrow { ProfessorNotFoundException("Profesor no encontrado") }
        val updated = Subject(id = existing.id, name = req.name, code = req.code, professor = prof)
        return mapper.toResponse(repository.save(updated))
    }
    fun delete(id: Long) {
        if (!repository.existsById(id)) throw SubjectNotFoundException("Materia no encontrada")
        repository.deleteById(id)
    }
}