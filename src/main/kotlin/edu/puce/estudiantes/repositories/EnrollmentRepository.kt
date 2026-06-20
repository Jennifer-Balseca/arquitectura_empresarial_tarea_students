package edu.puce.estudiantes.repositories

import edu.puce.estudiantes.entity.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnrollmentRepository : JpaRepository<Enrollment, Long>