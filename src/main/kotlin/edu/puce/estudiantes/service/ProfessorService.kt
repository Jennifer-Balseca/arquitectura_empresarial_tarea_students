package edu.puce.estudiantes.service

import edu.puce.estudiantes.Mappers.ProfessorMapper
import edu.puce.estudiantes.dto.ProfessorRequest
import edu.puce.estudiantes.dto.ProfessorResponse
import edu.puce.estudiantes.entity.Professor
import edu.puce.estudiantes.exceptions.BlankMesaggeException
import edu.puce.estudiantes.repository.ProfessorRepository
import org.springframework.stereotype.Service
import edu.puce.estudiantes.exceptions.ProfessorNotFoundException

@Service
class ProfessorService(private val repository: ProfessorRepository, private val mapper: ProfessorMapper) {
    fun create(req: ProfessorRequest): ProfessorResponse {
        if (req.name.isBlank()) throw BlankMesaggeException("El nombre no puede estar vacío")
        return mapper.toResponse(repository.save(mapper.toEntity(req)))
    }
    fun getAll() = repository.findAll().map { mapper.toResponse(it) }
    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { ProfessorNotFoundException("Profesor no encontrado") })
    fun update(id: Long, req: ProfessorRequest): ProfessorResponse {
        if (req.name.isBlank()) throw BlankMesaggeException("El nombre no puede estar vacío")
        val existing = repository.findById(id).orElseThrow { ProfessorNotFoundException("Profesor no encontrado") }
        val updated = Professor(id = existing.id, name = req.name, email = req.email)
        return mapper.toResponse(repository.save(updated))
    }
    fun delete(id: Long) {
        if (!repository.existsById(id)) throw ProfessorNotFoundException("Profesor no encontrado")
        repository.deleteById(id)
    }
}