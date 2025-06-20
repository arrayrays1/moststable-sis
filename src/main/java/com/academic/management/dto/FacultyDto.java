package com.academic.management.dto;

import com.academic.management.model.Role;

public class FacultyDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String facultyId;
    private Long courseId;
    private String courseName;
    private String generatedPassword; // For displaying the generated password once
    
    public FacultyDto() {
        super();
    }
    
    public FacultyDto(Long id, String name, String email, Role role, String facultyId, Long courseId, String courseName) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.facultyId = facultyId;
        this.courseId = courseId;
        this.courseName = courseName;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getFacultyId() {
        return facultyId;
    }
    
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getGeneratedPassword() {
        return generatedPassword;
    }
    
    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }
}

