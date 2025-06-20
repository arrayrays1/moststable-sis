package com.academic.management.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "faculty")
public class Faculty extends User {
    
    @Column(nullable = false, unique = true)
    private String facultyId;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @OneToMany(mappedBy = "faculty")
    private Set<Schedule> schedules = new HashSet<>();
    
    @Column(nullable = false)
    private Boolean active = true;
    
    public Faculty() {
        super();
    }
    
    public Faculty(Long id, String name, String email, String password, Role role, String facultyId, Course course) {
        super(id, name, email, password, role);
        this.facultyId = facultyId;
        this.course = course;
        this.active = true;
    }
    
    public String getFacultyId() {
        return facultyId;
    }
    
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Set<Schedule> getSchedules() {
        return schedules;
    }
    
    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
}
