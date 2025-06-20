package com.academic.management.service;

import com.academic.management.dto.*;
import com.academic.management.model.*;
import com.academic.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

@Autowired
private AdminRepository adminRepository;

@Autowired
private StudentRepository studentRepository;

@Autowired
private FacultyRepository facultyRepository;

@Autowired
private CourseRepository courseRepository;

@Autowired
private SubjectRepository subjectRepository;

@Autowired
private ScheduleRepository scheduleRepository;

@Autowired
private EnrollmentRepository enrollmentRepository;

@Autowired
private GradeRepository gradeRepository;

@Autowired
private PasswordEncoder passwordEncoder;

public String generateNextStudentId() {
    int currentYear = java.time.Year.now().getValue();
    String yearPrefix = currentYear + "-";
    
    // Get the highest student ID number for the current year from the database
    List<Student> allStudents = studentRepository.findAll();
    int maxNumber = 0;
    
    for (Student student : allStudents) {
        String studentId = student.getStudentId();
        if (studentId != null && studentId.startsWith(yearPrefix)) {
            try {
                String numberPart = studentId.substring(yearPrefix.length());
                int number = Integer.parseInt(numberPart);
                if (number > maxNumber) {
                    maxNumber = number;
                }
            } catch (NumberFormatException e) {
                // Skip invalid formats
            }
        }
    }
    
    int nextNumber = maxNumber + 1;
    return yearPrefix + String.format("%04d", nextNumber);
}

public String generateNextFacultyId() {
    int currentYear = java.time.Year.now().getValue();
    String yearPrefix = "F" + currentYear + "-";
    
    // Get the highest faculty ID number for the current year from the database
    List<Faculty> allFaculty = facultyRepository.findAll();
    int maxNumber = 0;
    
    for (Faculty faculty : allFaculty) {
        String facultyId = faculty.getFacultyId();
        if (facultyId != null && facultyId.startsWith(yearPrefix)) {
            try {
                String numberPart = facultyId.substring(yearPrefix.length());
                int number = Integer.parseInt(numberPart);
                if (number > maxNumber) {
                    maxNumber = number;
                }
            } catch (NumberFormatException e) {
                // Skip invalid formats
            }
        }
    }
    
    int nextNumber = maxNumber + 1;
    return yearPrefix + String.format("%03d", nextNumber);
}

@Transactional
public StudentDto createStudent(CreateStudentRequest request) {
    Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
    if (courseOptional.isEmpty()) {
        throw new RuntimeException("Course not found");
    }
    
    // Check if student ID already exists among all students (active and inactive)
    if (studentRepository.existsByStudentId(request.getStudentId())) {
        throw new RuntimeException("Student ID already exists");
    }
    
    Student student = new Student();
    student.setName(request.getName());
    student.setStudentId(request.getStudentId());
    student.setEmail(request.getEmail());
    student.setPassword(passwordEncoder.encode(request.getPassword()));
    student.setRole(Role.STUDENT);
    student.setCourse(courseOptional.get());
    student.setActive(true);
    
    student = studentRepository.save(student);
    
    StudentDto dto = new StudentDto();
    dto.setId(student.getId());
    dto.setName(student.getName());
    dto.setEmail(student.getEmail());
    dto.setRole(student.getRole());
    dto.setStudentId(student.getStudentId());
    dto.setCourseId(student.getCourse().getId());
    dto.setCourseName(student.getCourse().getName());
    dto.setGeneratedPassword(request.getPassword()); // Include the original password for display
    
    return dto;
}

@Transactional
public FacultyDto createFaculty(CreateFacultyRequest request) {
    Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
    if (courseOptional.isEmpty()) {
        throw new RuntimeException("Course not found");
    }
    
    // Check if faculty ID already exists among all faculty (active and inactive)
    if (facultyRepository.existsByFacultyId(request.getFacultyId())) {
        throw new RuntimeException("Faculty ID already exists");
    }
    
    // Check if email already exists among active faculty
    if (facultyRepository.existsByEmailAndActiveTrue(request.getEmail())) {
        throw new RuntimeException("Email already exists");
    }
    
    Faculty faculty = new Faculty();
    faculty.setName(request.getName());
    faculty.setFacultyId(request.getFacultyId());
    faculty.setEmail(request.getEmail());
    faculty.setPassword(passwordEncoder.encode(request.getPassword()));
    faculty.setRole(Role.FACULTY);
    faculty.setCourse(courseOptional.get());
    faculty.setActive(true);
    
    faculty = facultyRepository.save(faculty);
    
    FacultyDto dto = new FacultyDto();
    dto.setId(faculty.getId());
    dto.setName(faculty.getName());
    dto.setEmail(faculty.getEmail());
    dto.setRole(faculty.getRole());
    dto.setFacultyId(faculty.getFacultyId());
    dto.setCourseId(faculty.getCourse().getId());
    dto.setCourseName(faculty.getCourse().getName());
    dto.setGeneratedPassword(request.getPassword()); // Include the original password for display
    
    return dto;
}

@Transactional
public CourseDto createCourse(CreateCourseRequest request) {
    if (courseRepository.existsByCode(request.getCode())) {
        throw new RuntimeException("Course code already exists");
    }
    
    Course course = new Course();
    course.setCode(request.getCode());
    course.setName(request.getName());
    course.setDescription(request.getDescription());
    
    course = courseRepository.save(course);
    
    CourseDto dto = new CourseDto();
    dto.setId(course.getId());
    dto.setCode(course.getCode());
    dto.setName(course.getName());
    dto.setDescription(course.getDescription());
    
    return dto;
}

@Transactional
public SubjectDto createSubject(CreateSubjectRequest request) {
    Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
    if (courseOptional.isEmpty()) {
        throw new RuntimeException("Course not found");
    }
    
    Subject subject = new Subject();
    subject.setCode(request.getCode());
    subject.setName(request.getName());
    subject.setDescription(request.getDescription());
    subject.setCourse(courseOptional.get());
    
    subject = subjectRepository.save(subject);
    
    SubjectDto dto = new SubjectDto();
    dto.setId(subject.getId());
    dto.setCode(subject.getCode());
    dto.setName(subject.getName());
    dto.setDescription(subject.getDescription());
    dto.setCourseId(subject.getCourse().getId());
    dto.setCourseName(subject.getCourse().getName());
    
    return dto;
}

@Transactional
public ScheduleDto createSchedule(CreateScheduleRequest request) {
    Optional<Subject> subjectOptional = subjectRepository.findById(request.getSubjectId());
    if (subjectOptional.isEmpty()) {
        throw new RuntimeException("Subject not found");
    }
    
    Optional<Faculty> facultyOptional = facultyRepository.findByIdAndActive(request.getFacultyId());
    if (facultyOptional.isEmpty()) {
        throw new RuntimeException("Faculty not found");
    }
    
    Schedule schedule = new Schedule();
    schedule.setSubject(subjectOptional.get());
    schedule.setFaculty(facultyOptional.get());
    schedule.setDay(request.getDay());
    schedule.setStartTime(request.getStartTime());
    schedule.setEndTime(request.getEndTime());
    schedule.setRoom(request.getRoom());
    schedule.setSemester(request.getSemester());
    schedule.setAcademicYear(request.getAcademicYear());
    
    schedule = scheduleRepository.save(schedule);
    
    ScheduleDto dto = new ScheduleDto();
    dto.setId(schedule.getId());
    dto.setSubjectId(schedule.getSubject().getId());
    dto.setSubjectCode(schedule.getSubject().getCode());
    dto.setSubjectName(schedule.getSubject().getName());
    dto.setFacultyId(schedule.getFaculty().getId());
    dto.setFacultyName(schedule.getFaculty().getName());
    dto.setDay(schedule.getDay());
    dto.setStartTime(schedule.getStartTime());
    dto.setEndTime(schedule.getEndTime());
    dto.setRoom(schedule.getRoom());
    dto.setSemester(schedule.getSemester());
    dto.setAcademicYear(schedule.getAcademicYear());
    
    return dto;
}

@Transactional
public EnrollmentDto updateEnrollmentStatus(Long enrollmentId, UpdateEnrollmentStatusRequest request) {
    Optional<Enrollment> enrollmentOptional = enrollmentRepository.findById(enrollmentId);
    if (enrollmentOptional.isEmpty()) {
        throw new RuntimeException("Enrollment not found");
    }
    
    Enrollment enrollment = enrollmentOptional.get();
    enrollment.setStatus(request.getStatus());
    
    if (request.getStatus() == EnrollmentStatus.REJECTED && request.getRejectionReason() != null) {
        enrollment.setRejectionReason(request.getRejectionReason());
    }
    
    enrollment = enrollmentRepository.save(enrollment);
    
    return mapToEnrollmentDto(enrollment);
}

public List<EnrollmentDto> getPendingEnrollments() {
    return enrollmentRepository.findByStatus(EnrollmentStatus.PENDING)
            .stream()
            .map(this::mapToEnrollmentDto)
            .collect(Collectors.toList());
}

public List<StudentDto> getAllStudents() {
    return studentRepository.findAllActive()
            .stream()
            .map(this::mapToStudentDto)
            .collect(Collectors.toList());
}

public List<FacultyDto> getAllFaculty() {
    return facultyRepository.findAllActive()
            .stream()
            .map(this::mapToFacultyDto)
            .collect(Collectors.toList());
}

public List<CourseDto> getAllCourses() {
    return courseRepository.findAll()
            .stream()
            .map(this::mapToCourseDto)
            .collect(Collectors.toList());
}

public List<SubjectDto> getAllSubjects() {
    return subjectRepository.findAll()
            .stream()
            .map(this::mapToSubjectDto)
            .collect(Collectors.toList());
}

public List<ScheduleDto> getAllSchedules() {
    return scheduleRepository.findAll()
            .stream()
            .map(this::mapToScheduleDto)
            .collect(Collectors.toList());
}

@Transactional
public void deleteStudent(Long id) {
    Optional<Student> studentOptional = studentRepository.findByIdAndActive(id);
    if (studentOptional.isEmpty()) {
        throw new RuntimeException("Student not found");
    }
    
    // Soft delete - mark as inactive
    Student student = studentOptional.get();
    student.setActive(false);
    studentRepository.save(student);
}

@Transactional
public void deleteFaculty(Long id) {
    Optional<Faculty> facultyOptional = facultyRepository.findByIdAndActive(id);
    if (facultyOptional.isEmpty()) {
        throw new RuntimeException("Faculty not found");
    }
    
    // Soft delete - mark as inactive
    Faculty faculty = facultyOptional.get();
    faculty.setActive(false);
    facultyRepository.save(faculty);
}

public StudentDto getStudentById(Long id) {
    Optional<Student> studentOptional = studentRepository.findByIdAndActive(id);
    if (studentOptional.isEmpty()) {
        throw new RuntimeException("Student not found");
    }
    
    return mapToStudentDto(studentOptional.get());
}

public FacultyDto getFacultyById(Long id) {
    Optional<Faculty> facultyOptional = facultyRepository.findByIdAndActive(id);
    if (facultyOptional.isEmpty()) {
        throw new RuntimeException("Faculty not found");
    }
    
    return mapToFacultyDto(facultyOptional.get());
}

@Transactional
public FacultyDto updateFaculty(Long id, CreateFacultyRequest request) {
    Optional<Faculty> facultyOptional = facultyRepository.findByIdAndActive(id);
    if (facultyOptional.isEmpty()) {
        throw new RuntimeException("Faculty not found");
    }
    
    Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
    if (courseOptional.isEmpty()) {
        throw new RuntimeException("Course not found");
    }
    
    Faculty faculty = facultyOptional.get();
    
    // Check if faculty ID is being changed and if it already exists
    if (!faculty.getFacultyId().equals(request.getFacultyId()) && 
        facultyRepository.existsByFacultyId(request.getFacultyId())) {
        throw new RuntimeException("Faculty ID already exists");
    }
    
    // Check if email is being changed and if it already exists among active faculty
    if (!faculty.getEmail().equals(request.getEmail()) && 
        facultyRepository.existsByEmailAndActiveTrue(request.getEmail())) {
        throw new RuntimeException("Email already exists");
    }
    
    faculty.setName(request.getName());
    faculty.setFacultyId(request.getFacultyId());
    faculty.setEmail(request.getEmail());
    faculty.setCourse(courseOptional.get());
    
    // Update password only if provided
    if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
        faculty.setPassword(passwordEncoder.encode(request.getPassword()));
    }
    
    faculty = facultyRepository.save(faculty);
    
    return mapToFacultyDto(faculty);
}

@Transactional
public StudentDto updateStudent(Long id, CreateStudentRequest request) {
    Optional<Student> studentOptional = studentRepository.findByIdAndActive(id);
    if (studentOptional.isEmpty()) {
        throw new RuntimeException("Student not found");
    }
    
    Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
    if (courseOptional.isEmpty()) {
        throw new RuntimeException("Course not found");
    }
    
    Student student = studentOptional.get();
    
    // Check if student ID is being changed and if it already exists
    if (!student.getStudentId().equals(request.getStudentId()) && 
        studentRepository.existsByStudentId(request.getStudentId())) {
        throw new RuntimeException("Student ID already exists");
    }
    
    student.setName(request.getName());
    student.setStudentId(request.getStudentId());
    student.setEmail(request.getEmail());
    student.setCourse(courseOptional.get());
    
    // Update password only if provided
    if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
        student.setPassword(passwordEncoder.encode(request.getPassword()));
    }
    
    student = studentRepository.save(student);
    
    return mapToStudentDto(student);
}

private StudentDto mapToStudentDto(Student student) {
    StudentDto dto = new StudentDto();
    dto.setId(student.getId());
    dto.setName(student.getName());
    dto.setEmail(student.getEmail());
    dto.setRole(student.getRole());
    dto.setStudentId(student.getStudentId());
    dto.setCourseId(student.getCourse().getId());
    dto.setCourseName(student.getCourse().getName());
    return dto;
}

private FacultyDto mapToFacultyDto(Faculty faculty) {
    FacultyDto dto = new FacultyDto();
    dto.setId(faculty.getId());
    dto.setName(faculty.getName());
    dto.setEmail(faculty.getEmail());
    dto.setRole(faculty.getRole());
    dto.setFacultyId(faculty.getFacultyId());
    dto.setCourseId(faculty.getCourse().getId());
    dto.setCourseName(faculty.getCourse().getName());
    return dto;
}

private CourseDto mapToCourseDto(Course course) {
    CourseDto dto = new CourseDto();
    dto.setId(course.getId());
    dto.setCode(course.getCode());
    dto.setName(course.getName());
    dto.setDescription(course.getDescription());
    return dto;
}

private SubjectDto mapToSubjectDto(Subject subject) {
    SubjectDto dto = new SubjectDto();
    dto.setId(subject.getId());
    dto.setCode(subject.getCode());
    dto.setName(subject.getName());
    dto.setDescription(subject.getDescription());
    dto.setCourseId(subject.getCourse().getId());
    dto.setCourseName(subject.getCourse().getName());
    return dto;
}

private ScheduleDto mapToScheduleDto(Schedule schedule) {
    ScheduleDto dto = new ScheduleDto();
    dto.setId(schedule.getId());
    dto.setSubjectId(schedule.getSubject().getId());
    dto.setSubjectCode(schedule.getSubject().getCode());
    dto.setSubjectName(schedule.getSubject().getName());
    dto.setFacultyId(schedule.getFaculty().getId());
    dto.setFacultyName(schedule.getFaculty().getName());
    dto.setDay(schedule.getDay());
    dto.setStartTime(schedule.getStartTime());
    dto.setEndTime(schedule.getEndTime());
    dto.setRoom(schedule.getRoom());
    dto.setSemester(schedule.getSemester());
    dto.setAcademicYear(schedule.getAcademicYear());
    return dto;
}

private EnrollmentDto mapToEnrollmentDto(Enrollment enrollment) {
    EnrollmentDto dto = new EnrollmentDto();
    dto.setId(enrollment.getId());
    dto.setStudentId(enrollment.getStudent().getId());
    dto.setStudentName(enrollment.getStudent().getName());
    dto.setStudentIdNumber(enrollment.getStudent().getStudentId());
    dto.setSubjectId(enrollment.getSubject().getId());
    dto.setSubjectCode(enrollment.getSubject().getCode());
    dto.setSubjectName(enrollment.getSubject().getName());
    dto.setSemester(enrollment.getSemester());
    dto.setAcademicYear(enrollment.getAcademicYear());
    dto.setEnrollmentDate(enrollment.getEnrollmentDate());
    dto.setStatus(enrollment.getStatus());
    dto.setRejectionReason(enrollment.getRejectionReason());
    return dto;
}
}
