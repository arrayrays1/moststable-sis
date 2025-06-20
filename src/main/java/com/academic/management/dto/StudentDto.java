package com.academic.management.dto;

import com.academic.management.model.Role;

public class StudentDto extends UserDto {
    private String studentId;
    private Long courseId;
    private String courseName;
    private String generatedPassword;

    public StudentDto() {
        super();
    }
    
    public StudentDto(Long id, String name, String email, Role role, String studentId, Long courseId, String courseName) {
        super(id, name, email, role);
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseName = courseName;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
