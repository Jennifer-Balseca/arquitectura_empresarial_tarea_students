package edu.puce.estudiantes.dto

data class EnrollmentResponse(
    val id: Long,
    val createdAt: String,
    val status: String,
    val student: StudentResponse,
    val subject: SubjectResponse,
)