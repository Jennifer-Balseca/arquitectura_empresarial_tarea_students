package edu.puce.estudiantes.controller

import edu.puce.estudiantes.dto.ProfessorRequest
import edu.puce.estudiantes.service.ProfessorService
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
@RequestMapping("/api/professors")
class ProfessorController(private val service: ProfessorService) {

    @PostMapping
    fun create(@RequestBody request: ProfessorRequest): ResponseEntity<Any> {
        return ResponseEntity(service.create(request), HttpStatus.CREATED)
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
    fun update(@PathVariable id: Long, @RequestBody request: ProfessorRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(service.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}