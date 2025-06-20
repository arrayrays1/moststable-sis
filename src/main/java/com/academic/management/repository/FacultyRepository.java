package com.academic.management.repository;

import com.academic.management.model.Course;
import com.academic.management.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByCourseAndActiveTrue(Course course);
    boolean existsByFacultyIdAndActiveTrue(String facultyId);
    boolean existsByEmailAndActiveTrue(String email);
    
    @Query("SELECT f FROM Faculty f WHERE f.active = true")
    List<Faculty> findAllActive();
    
    @Query("SELECT f FROM Faculty f WHERE f.id = ?1 AND f.active = true")
    Optional<Faculty> findByIdAndActive(Long id);
    
    // Keep the original methods for backward compatibility
    List<Faculty> findByCourse(Course course);
    boolean existsByFacultyId(String facultyId);
    boolean existsByEmail(String email);
}
