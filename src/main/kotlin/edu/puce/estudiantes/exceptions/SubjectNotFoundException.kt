package edu.puce.estudiantes.exceptions
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class SubjectNotFoundException(message: String) : RuntimeException(message)