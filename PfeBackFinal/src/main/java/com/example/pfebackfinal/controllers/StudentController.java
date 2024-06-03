package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.presistence.entity.Student;
import com.example.pfebackfinal.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/student")

public class StudentController {

    private final StudentService pupilService;

    @PostMapping
    public Student createPupil(@RequestParam String email, @RequestParam String name) {
        return pupilService.createPupil(email, name);
    }
}
