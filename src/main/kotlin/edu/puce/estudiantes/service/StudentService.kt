package edu.puce.estudiantes.service

import edu.puce.estudiantes.dto.StudentRequest
import edu.puce.estudiantes.dto.StudentResponse
import edu.puce.estudiantes.mapper.StudentMapper
import edu.puce.estudiantes.entity.Student
import edu.puce.estudiantes.exceptions.BlankMesaggeException
import edu.puce.estudiantes.repositories.StudentRepository
import org.springframework.stereotype.Service
import edu.puce.estudiantes.exceptions.StudentNotFoundException

@Service
class StudentService(private val repository: StudentRepository, private val mapper: StudentMapper) {
    fun create(req: StudentRequest): StudentResponse {
        if (req.name.isBlank()) throw BlankMesaggeException("El nombre no puede estar vacío")
        return mapper.toResponse(repository.save(mapper.toEntity(req)))
    }
    fun getAll() = repository.findAll().map { mapper.toResponse(it) }
    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { StudentNotFoundException("Estudiante no encontrado") })
    fun update(id: Long, req: StudentRequest): StudentResponse {
        if (req.name.isBlank()) throw BlankMesaggeException("El nombre no puede estar vacío")
        val existing = repository.findById(id).orElseThrow { StudentNotFoundException("Estudiante no encontrado") }
        val updated = Student(id = existing.id, name = req.name, email = req.email)
        return mapper.toResponse(repository.save(updated))
    }
    fun delete(id: Long) {
        if (!repository.existsById(id)) throw StudentNotFoundException("Estudiante no encontrado")
        repository.deleteById(id)
    }
}