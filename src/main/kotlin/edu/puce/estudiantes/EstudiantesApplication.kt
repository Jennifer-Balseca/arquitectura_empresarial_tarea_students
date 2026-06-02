package edu.puce.estudiantes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EstudiantesApplication

fun main(args: Array<String>) {
	runApplication<EstudiantesApplication>(*args)
}
