package edu.puce.estudiantes.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalException {

    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFound(e: StudentNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionMessage(e.message ?: "Estudiante no encontrado", "StudentService"))

    @ExceptionHandler(ProfessorNotFoundException::class)
    fun handleProfessorNotFound(e: ProfessorNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionMessage(e.message ?: "Profesor no encontrado", "ProfessorService"))

    @ExceptionHandler(SubjectNotFoundException::class)
    fun handleSubjectNotFound(e: SubjectNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionMessage(e.message ?: "Materia no encontrada", "SubjectService"))

    @ExceptionHandler(EnrollmentNotFoundException::class)
    fun handleEnrollmentNotFound(e: EnrollmentNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionMessage(e.message ?: "Inscripción no encontrada", "EnrollmentService"))

    @ExceptionHandler(BlankMesaggeException::class)
    fun handleBlank(e: BlankMesaggeException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionMessage(e.message ?: "Datos inválidos", "Validation"))
}

data class ExceptionMessage(val message: String, val source: String)