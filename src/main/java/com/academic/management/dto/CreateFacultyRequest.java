package com.academic.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateFacultyRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Faculty ID is required")
    private String facultyId;
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    public CreateFacultyRequest() {
        super();
    }
    
    public CreateFacultyRequest(String name, String facultyId, Long courseId, String email, String password) {
        super();
        this.name = name;
        this.facultyId = facultyId;
        this.courseId = courseId;
        this.email = email;
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
