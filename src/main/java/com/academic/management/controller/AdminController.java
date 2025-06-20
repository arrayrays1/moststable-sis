package com.academic.management.controller;

import com.academic.management.dto.*;
import com.academic.management.model.Role;
import com.academic.management.service.AdminService;
import com.academic.management.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

@Autowired
private AdminService adminService;

@Autowired
private UserService userService;

@GetMapping("/dashboard")
public String dashboard(HttpSession session, Model model) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    model.addAttribute("user", currentUser);
    model.addAttribute("pendingEnrollments", adminService.getPendingEnrollments());
    return "admin/dashboard";
}

@GetMapping("/students")
public String students(HttpSession session, Model model) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    model.addAttribute("user", currentUser);
    model.addAttribute("students", adminService.getAllStudents());
    model.addAttribute("courses", adminService.getAllCourses());
    return "admin/students";
}

@GetMapping("/students/{id}")
@ResponseBody
public ResponseEntity<?> getStudent(@PathVariable("id") Long id, HttpSession session) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    try {
        StudentDto student = adminService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>("Error fetching student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/api/next-student-id")
@ResponseBody
public ResponseEntity<String> getNextStudentId(HttpSession session) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
    
    try {
        String nextStudentId = adminService.generateNextStudentId();
        return new ResponseEntity<>(nextStudentId, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>("Error generating student ID", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/api/next-faculty-id")
@ResponseBody
public ResponseEntity<String> getNextFacultyId(HttpSession session) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
    
    try {
        String nextFacultyId = adminService.generateNextFacultyId();
        return new ResponseEntity<>(nextFacultyId, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>("Error generating faculty ID", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@PostMapping("/students/{id}/update")
public String updateStudent(@PathVariable("id") Long id, 
                           @ModelAttribute CreateStudentRequest request, 
                           BindingResult bindingResult,
                           HttpSession session, 
                           RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }

    // Basic validation
    if (request.getName() == null || request.getName().trim().isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Student name is required");
        return "redirect:/admin/students";
    }
    
    if (request.getStudentId() == null || request.getStudentId().trim().isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Student ID is required");
        return "redirect:/admin/students";
    }
    
    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Email is required");
        return "redirect:/admin/students";
    }
    
    if (request.getCourseId() == null) {
        redirectAttributes.addFlashAttribute("errorMessage", "Course selection is required");
        return "redirect:/admin/students";
    }

    try {
        adminService.updateStudent(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error updating student: " + e.getMessage());
    }
    return "redirect:/admin/students";
}

@PostMapping("/students/{id}/delete")
public String deleteStudent(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }

    try {
        adminService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
    }
    return "redirect:/admin/students";
}

@GetMapping("/faculty")
public String faculty(HttpSession session, Model model) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    model.addAttribute("user", currentUser);
    model.addAttribute("faculty", adminService.getAllFaculty());
    model.addAttribute("courses", adminService.getAllCourses());
    return "admin/faculty";
}

@GetMapping("/faculty/{id}")
@ResponseBody
public ResponseEntity<?> getFaculty(@PathVariable("id") Long id, HttpSession session) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    try {
        FacultyDto faculty = adminService.getFacultyById(id);
        return new ResponseEntity<>(faculty, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>("Error fetching faculty: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@PostMapping("/faculty/{id}/update")
public String updateFaculty(@PathVariable("id") Long id, 
                           @ModelAttribute CreateFacultyRequest request, 
                           BindingResult bindingResult,
                           HttpSession session, 
                           RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }

    // Basic validation
    if (request.getName() == null || request.getName().trim().isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Faculty name is required");
        return "redirect:/admin/faculty";
    }
    
    if (request.getFacultyId() == null || request.getFacultyId().trim().isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Faculty ID is required");
        return "redirect:/admin/faculty";
    }
    
    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Email is required");
        return "redirect:/admin/faculty";
    }
    
    if (request.getCourseId() == null) {
        redirectAttributes.addFlashAttribute("errorMessage", "Course selection is required");
        return "redirect:/admin/faculty";
    }

    try {
        adminService.updateFaculty(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Faculty updated successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error updating faculty: " + e.getMessage());
    }
    return "redirect:/admin/faculty";
}

@PostMapping("/faculty/{id}/delete")
public String deleteFaculty(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }

    try {
        adminService.deleteFaculty(id);
        redirectAttributes.addFlashAttribute("successMessage", "Faculty deleted successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error deleting faculty: " + e.getMessage());
    }
    return "redirect:/admin/faculty";
}

@GetMapping("/courses")
public String courses(HttpSession session, Model model) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    model.addAttribute("user", currentUser);
    model.addAttribute("courses", adminService.getAllCourses());
    return "admin/courses";
}

@GetMapping("/subjects")
public String subjects(HttpSession session, Model model) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    model.addAttribute("user", currentUser);
    model.addAttribute("subjects", adminService.getAllSubjects());
    model.addAttribute("courses", adminService.getAllCourses());
    return "admin/subjects";
}

@GetMapping("/schedules")
public String schedules(HttpSession session, Model model) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    model.addAttribute("user", currentUser);
    model.addAttribute("schedules", adminService.getAllSchedules());
    model.addAttribute("subjects", adminService.getAllSubjects());
    model.addAttribute("faculty", adminService.getAllFaculty());
    return "admin/schedules";
}

@GetMapping("/enrollments")
public String enrollments(HttpSession session, Model model) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    model.addAttribute("user", currentUser);
    model.addAttribute("pendingEnrollments", adminService.getPendingEnrollments());
    return "admin/enrollments";
}

@PostMapping("/students/create")
public String createStudent(@Valid CreateStudentRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    try {
        StudentDto student = adminService.createStudent(request);
        redirectAttributes.addFlashAttribute("successMessage", "Student created successfully");
        redirectAttributes.addFlashAttribute("newStudent", student);
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error creating student: " + e.getMessage());
    }
    return "redirect:/admin/students";
}

@PostMapping("/faculty/create")
public String createFaculty(@Valid CreateFacultyRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    try {
        FacultyDto faculty = adminService.createFaculty(request);
        redirectAttributes.addFlashAttribute("successMessage", "Faculty created successfully");
        redirectAttributes.addFlashAttribute("newFaculty", faculty);
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error creating faculty: " + e.getMessage());
    }
    return "redirect:/admin/faculty";
}

@PostMapping("/courses/create")
public String createCourse(@Valid CreateCourseRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    try {
        adminService.createCourse(request);
        redirectAttributes.addFlashAttribute("successMessage", "Course created successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error creating course: " + e.getMessage());
    }
    return "redirect:/admin/courses";
}

@PostMapping("/subjects/create")
public String createSubject(@Valid CreateSubjectRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    try {
        adminService.createSubject(request);
        redirectAttributes.addFlashAttribute("successMessage", "Subject created successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error creating subject: " + e.getMessage());
    }
    return "redirect:/admin/subjects";
}

@PostMapping("/schedules/create")
public String createSchedule(@Valid CreateScheduleRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    try {
        adminService.createSchedule(request);
        redirectAttributes.addFlashAttribute("successMessage", "Schedule created successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error creating schedule: " + e.getMessage());
    }
    return "redirect:/admin/schedules";
}

@PostMapping("/enrollments/{id}/update-status")
public String updateEnrollmentStatus(@PathVariable("id") Long enrollmentId, 
                                     @Valid UpdateEnrollmentStatusRequest request, 
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
    UserDto currentUser = userService.getCurrentUser(session);
    if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
        return "redirect:/auth/login";
    }
    
    try {
        adminService.updateEnrollmentStatus(enrollmentId, request);
        redirectAttributes.addFlashAttribute("successMessage", "Enrollment status updated successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error updating enrollment status: " + e.getMessage());
    }
    return "redirect:/admin/enrollments";
}
}
