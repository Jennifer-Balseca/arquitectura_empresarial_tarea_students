package edu.puce.estudiantes.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "subjects")
class Subject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    val code: String,
    @ManyToOne
    @JoinColumn(name = "professor_id")
    val professor: Professor
)


//PARA QUE TODA LA DATA SE CARGE DE UNA SOLA: EAGER
//PARA ACCEDER A DATA MANUALMENTE: LAZY (asi se ahorra recursos)