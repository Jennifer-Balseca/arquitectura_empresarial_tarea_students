package edu.puce.estudiantes.repositories

import edu.puce.estudiantes.entity.Professor
import org.springframework.data.jpa.repository.JpaRepository

interface ProfessorRepository : JpaRepository<Professor, Long>