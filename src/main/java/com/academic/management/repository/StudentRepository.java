package com.academic.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.academic.management.model.Course;
import com.academic.management.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCourseAndActiveTrue(Course course);
    boolean existsByStudentIdAndActiveTrue(String studentId);
    
    @Query("SELECT s FROM Student s WHERE s.active = true")
    List<Student> findAllActive();
    
    @Query("SELECT s FROM Student s WHERE s.id = ?1 AND s.active = true")
    Optional<Student> findByIdAndActive(Long id);
    
    // Keep the original methods for backward compatibility
    List<Student> findByCourse(Course course);
    boolean existsByStudentId(String studentId);

    @Query(value = "SELECT student_id FROM students WHERE student_id IS NOT NULL ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastStudentId();

    
}
