package edu.puce.estudiantes.dto

data class SubjectRequest(
    val name: String,
    val code: String,
    val professorId: Long,
)

