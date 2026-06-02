package edu.puce.estudiantes.controller

import edu.puce.estudiantes.dto.StudentRequest
import edu.puce.estudiantes.dto.StudentResponse
import edu.puce.estudiantes.service.StudentService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
open class StudentController(
    val studentService: StudentService
) {

    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping("/students")
    open fun createStudent(
        @RequestBody
        request: StudentRequest
    ): StudentResponse {
        logger.info("Creating student ${request.name}")
        return studentService.createStudent(request)
    }

    @GetMapping("/students")
    open fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        return studentService.getAllStudents()
    }


}