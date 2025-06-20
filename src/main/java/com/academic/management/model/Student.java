package com.academic.management.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student extends User {
    
    @Column(nullable = false, unique = true)
    private String studentId;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @OneToMany(mappedBy = "student")
    private Set<Grade> grades = new HashSet<>();
    
    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments = new HashSet<>();
    
    @Column(nullable = false)
    private Boolean active = true;
    
    public Student() {
        super();
    }
    
    public Student(Long id, String name, String email, String password, Role role, String studentId, Course course) {
        super(id, name, email, password, role);
        this.studentId = studentId;
        this.course = course;
        this.active = true;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Set<Grade> getGrades() {
        return grades;
    }
    
    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }
    
    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
}
