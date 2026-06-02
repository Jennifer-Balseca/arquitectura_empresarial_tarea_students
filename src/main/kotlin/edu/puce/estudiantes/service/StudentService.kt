package edu.puce.estudiantes.service

import edu.puce.estudiantes.dto.StudentRequest
import edu.puce.estudiantes.dto.StudentResponse
import edu.puce.estudiantes.entity.Student
import edu.puce.estudiantes.repository.StudentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
open class StudentService(
    private val studentRepository: StudentRepository
) {

    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    open fun createStudent(request: StudentRequest): StudentResponse {
        logger.info("Creating student ${request.name}")

        // validar

        // crear entidad
        val studentEntity = Student(
            name = request.name,
            email = request.email
        )

        // guardar entidad
        val savedStudent = studentRepository.save(studentEntity)

        // retornar response
        return StudentResponse(
            id = savedStudent.id ?: 0L,
            name = savedStudent.name,
            email = savedStudent.email
        )
    }

    open fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")

        // consultar todos los estudiantes
        val savedStudents = studentRepository.findAll()

        // convertir al response adecuado
        return savedStudents.map {
            StudentResponse(
                id = it.id ?: 0L,
                name = it.name,
                email = it.email
            )
        }
    }
}