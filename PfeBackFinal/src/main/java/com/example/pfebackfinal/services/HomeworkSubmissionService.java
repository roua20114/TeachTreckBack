package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.HomeworkSubmission;
import com.example.pfebackfinal.presistence.entity.Student;
import com.example.pfebackfinal.presistence.repository.HomeworkSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor

public class HomeworkSubmissionService implements IHomeworkSubmissionService {

    private final HomeworkSubmissionRepository submissionRepository; // Assuming you have a repository for HomeworkSubmission


    private final ClassroomService classroomService;


    private final StudentService studentService;
    @Override
    public void submitHomework(String studentId, String classroomId, String homeworkContent) {

            // Retrieve student and classroom objects
            Student student = studentService.getStudent(studentId);
            Classroom classroom = classroomService.getClassroom(classroomId);

            // Create a new homework submission
            HomeworkSubmission submission = new HomeworkSubmission();
            submission.setStudent(student);
            submission.setClassroom(classroom);
            submission.setHomeworkContent(homeworkContent);
            submission.setSubmittedAt(new Date());

            // Save the submission to the repository
            submissionRepository.save(submission);

            // Optionally, notify relevant parties (teachers, administrators) about the submission
            // Example: Send a notification or update the classroom entity with the submission

    }
    @Override
    public List<HomeworkSubmission> getHomeworkSubmissionsByClassroom(String classroomId) {
        Classroom classroom = classroomService.getClassroom(classroomId);
        return submissionRepository.findByClassroom(classroom);
    }
}
