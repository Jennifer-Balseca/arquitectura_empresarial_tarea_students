package edu.puce.estudiantes.repository

import edu.puce.estudiantes.entity.Professor
import org.springframework.data.jpa.repository.JpaRepository

interface ProfessorRepository : JpaRepository<Professor, Long>