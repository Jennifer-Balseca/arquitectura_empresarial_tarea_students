package edu.puce.estudiantes.controller

import edu.puce.estudiantes.dto.SubjectRequest
import edu.puce.estudiantes.service.SubjectService
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
@RequestMapping("/api/subjects")
class SubjectController(private val service: SubjectService) {

    @PostMapping
    fun create(@RequestBody req: SubjectRequest) =
        ResponseEntity(service.create(req), HttpStatus.CREATED)

    @GetMapping
    fun getAll() =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
    ResponseEntity.ok(service.getById(id))

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: SubjectRequest) =
        ResponseEntity.ok(service.update(id, req))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}