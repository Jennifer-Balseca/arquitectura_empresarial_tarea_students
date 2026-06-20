package edu.puce.estudiantes.controller

import edu.puce.estudiantes.dto.EnrollmentRequest
import edu.puce.estudiantes.service.EnrollmentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(private val service: EnrollmentService) {

    @PostMapping
    fun enroll(@RequestBody request: EnrollmentRequest): ResponseEntity<Any> {
        return ResponseEntity(service.enroll(request), HttpStatus.CREATED)
    }

    @GetMapping
    fun getAll(): ResponseEntity<Any> {
        return ResponseEntity.ok(service.getAll())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        return ResponseEntity.ok(service.getById(id))
    }

    @PutMapping("/{id}")
    fun updateStatus(@PathVariable id: Long, @RequestBody statusMap: Map<String, String>): ResponseEntity<Any> {
        val newStatus = statusMap["status"] ?: ""
        return ResponseEntity.ok(service.updateStatus(id, newStatus))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}