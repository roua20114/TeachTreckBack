package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.presistence.repository.ClassroomRepository;
import com.example.pfebackfinal.presistence.repository.JoinRequestRepository;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JoinRequestService {
    private final ClassroomService classroomService;
    private final StudentService studentService;
    private final JoinRequestRepository joinRequestRepository;
    private final UserRepository userRepository;
    private final ClassroomRepository classroomRepository;

    public JoinRequest sendJoinRequest(String classroomId, Student student) {
        JoinRequest request = new JoinRequest();
        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        request.setClassroom(classroom);// assuming Classroom only needs an ID here
        request.setStudent(student);
        request.setStatus("PENDING");
        return joinRequestRepository.save(request);
    }




    public List<JoinRequest> getPendingRequestsForClassroom(String classroomId) {
        return joinRequestRepository.findByClassroomIdAndStatus(classroomId, "PENDING");
    }
    public JoinRequest updateRequestStatus(String requestId, String status) {
        JoinRequest request = joinRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        return joinRequestRepository.save(request);
    }

//    public List<Classroom> getApprovedClassroomsForStudent(String studentId) {
//        return joinRequestRepository.findByStudentId(studentId)
//                .stream()
//                .filter(req -> "APPROVED".equals(req.getStatus()))
//                .map(JoinRequest::getClassroom)
//                .collect(Collectors.toList());
//    }



    public List<String> getAllStudentEmailsInClassroom(String classroomId) {
        // Retrieve the classroom by ID
        Classroom classroom = classroomService.getClassroom(classroomId);

        // Get emails of students added directly to the classroom
        List<String> studentEmails = new ArrayList<>(classroom.getStudentEmails());

        // Fetch join requests for this classroom that are approved
        List<JoinRequest> approvedRequests = joinRequestRepository.findByClassroomIdAndStatus(classroomId, "approved");

        // Add emails of students with approved join requests
        for (JoinRequest request : approvedRequests) {
            studentEmails.add(request.getStudent().getEmail());
        }

        // Remove duplicates
        return studentEmails.stream().distinct().collect(Collectors.toList());
    }
    public List<Classroom> getAllApprovedClassrooms() {
        return joinRequestRepository.findByStatus("APPROVED")
                .stream()
                .map(JoinRequest::getClassroom)
                .collect(Collectors.toList());
    }

    public List<ApprovedJoinRequestDTO> getAllApprovedJoinRequestsWithStudentEmails() {
        List<JoinRequest> approvedRequests = joinRequestRepository.findByStatus("APPROVED");

        return approvedRequests.stream().map(request ->
                new ApprovedJoinRequestDTO(
                        request.getClassroom().getId(),
                        request.getClassroom().getName(),
                        request.getStudent().getEmail(),
                        request.getStatus()
                )
        ).collect(Collectors.toList());
    }
    public List<String> getApprovedStudentEmailsForClassroom(String classroomId) {
        return joinRequestRepository.findByClassroom_IdAndStatus(classroomId, "APPROVED")
                .stream()
                .map(joinRequest -> joinRequest.getStudent().getEmail())
                .collect(Collectors.toList());
    }
}
