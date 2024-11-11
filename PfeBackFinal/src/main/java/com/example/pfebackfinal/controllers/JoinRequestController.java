package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.presistence.entity.ApprovedJoinRequestDTO;
import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.JoinRequest;
import com.example.pfebackfinal.presistence.entity.Student;
import com.example.pfebackfinal.services.ClassroomService;
import com.example.pfebackfinal.services.JoinRequestService;
import com.example.pfebackfinal.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/join-requests")
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:4200")
public class JoinRequestController {
    private final StudentService studentService;
    private final ClassroomService classroomService;


    private final JoinRequestService joinRequestService;

//    @PostMapping("/request")
//    public ResponseEntity<JoinRequest> createJoinRequest(@RequestParam String classroomId, @RequestParam String studentEmail) {
//        JoinRequest joinRequest = joinRequestService.createJoinRequest(classroomId, studentEmail);
//        return new ResponseEntity<>(joinRequest, HttpStatus.CREATED);
//    }

//    @GetMapping("/pending/{classroomId}")
//    public ResponseEntity<List<JoinRequest>> getPendingRequests(@PathVariable String classroomId) {
//        return ResponseEntity.ok(joinRequestService.getPendingRequests(classroomId));
//    }

    @PostMapping("/send")
    public ResponseEntity<?> sendJoinRequest(
            @RequestParam String studentId,
            @RequestParam String classroomId) {

        // Check for missing parameters
        if (studentId == null || classroomId == null) {
            return ResponseEntity.badRequest().body("Missing studentId or classroomId");
        }

        Student student = studentService.findById(studentId); // Fetch the student object
        Classroom classroom = classroomService.findById(classroomId); // Fetch classroom object

        if (student == null || classroom == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student or Classroom not found");
        }

        JoinRequest request = joinRequestService.sendJoinRequest(classroomId, student);
        return ResponseEntity.ok(request);
    }
//    @GetMapping("/classroom/{classroomId}")
//    public ResponseEntity<List<JoinRequest>> getJoinRequests(@PathVariable String classroomId) {
//        List<JoinRequest> requests = joinRequestService.getJoinRequestsForClassroom(classroomId);
//        return new ResponseEntity<>(requests, HttpStatus.OK);
//    }

    @GetMapping("/classroom/{classroomId}/pending")
    public List<JoinRequest> getPendingRequests(@PathVariable String classroomId) {
        return joinRequestService.getPendingRequestsForClassroom(classroomId);
    }

//    @PutMapping("/approve/{requestId}")
//    public ResponseEntity<JoinRequest> approveRequest(@PathVariable String requestId) {
//        JoinRequest request = joinRequestService.approveRequest(requestId);
//        return new ResponseEntity<>(request, HttpStatus.OK);
//    }
    @PostMapping("/{requestId}/status")
    public JoinRequest updateRequestStatus(@PathVariable String requestId, @RequestParam String status) {
        return joinRequestService.updateRequestStatus(requestId, status);
    }
    @GetMapping("/approved")
    public ResponseEntity<List<ApprovedJoinRequestDTO>> getAllApprovedJoinRequestsWithStudentEmails() {
        List<ApprovedJoinRequestDTO> approvedRequests = joinRequestService.getAllApprovedJoinRequestsWithStudentEmails();
        return ResponseEntity.ok(approvedRequests);
    }

    @GetMapping("/{classroomId}/approved-emails")
    public ResponseEntity<List<String>> getApprovedStudentEmailsForClassroom(@PathVariable String classroomId) {
        List<String> approvedStudentEmails = joinRequestService.getApprovedStudentEmailsForClassroom(classroomId);
        return ResponseEntity.ok(approvedStudentEmails);
    }
}
