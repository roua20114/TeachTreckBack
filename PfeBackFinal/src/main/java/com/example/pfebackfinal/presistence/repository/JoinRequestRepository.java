package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.JoinRequest;
import com.example.pfebackfinal.presistence.entity.JoinRequestStatus;
import com.example.pfebackfinal.presistence.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JoinRequestRepository extends MongoRepository<JoinRequest,String> {
    boolean existsByStudentAndClassroomAndStatus(Student student, Classroom classroom, JoinRequestStatus joinRequestStatus);
    List<JoinRequest> findByClassroom(Classroom classroom);
    List<JoinRequest> findByStudentIdAndStatusTrue(String studentId);
    List<JoinRequest> findByClassroomIdAndStatus(String classroomId, String status);
    List<JoinRequest> findByClassroomId(String classroomId);
    List<JoinRequest> findByStudentId(String studentId);
    List<JoinRequest> findByStudentIdAndStatus(String studentId, String status);
    List<JoinRequest> findByStatus(String status);
    List<JoinRequest> findByClassroom_IdAndStatus(String classroomId, String status);



}
