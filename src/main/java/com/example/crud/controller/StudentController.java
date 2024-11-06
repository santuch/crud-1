package com.example.crud.controller;

import com.example.crud.Student;
import com.example.crud.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get a student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    // Update an existing student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentRepository.findById(id).map(student -> {
            student.setUserName(studentDetails.getUserName());
            student.setType(studentDetails.getType());
            student.setDisplayNameEn(studentDetails.getDisplayNameEn()); 
            student.setDisplayNameTh(studentDetails.getDisplayNameTh()); 
            student.setEmail(studentDetails.getEmail());
            student.setFaculty(studentDetails.getFaculty());
            student.setDepartment(studentDetails.getDepartment()); 
            student.setTuStatus(studentDetails.getTuStatus()); 
            student.setStatusWork(studentDetails.getStatusWork()); 
            student.setStatusEmp(studentDetails.getStatusEmp()); 
            studentRepository.save(student);
            return ResponseEntity.ok(student);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Save user data from TU API
    @PostMapping("/saveUser")
    public ResponseEntity<Student> saveUserData(@RequestBody Student userData) {
        Student savedStudent = studentRepository.save(userData);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }
}
