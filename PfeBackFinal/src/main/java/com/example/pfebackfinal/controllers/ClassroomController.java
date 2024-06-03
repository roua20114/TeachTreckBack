package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.services.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/classroom")
@CrossOrigin(origins = "http://localhost:4200")

public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping
    public Classroom createClassroom(@RequestParam String name) {
        return classroomService.createClassroom(name);
    }

    @PostMapping("/{code}/students")
    public Classroom addPupilToClassroom(@PathVariable String code, @RequestParam String email) {
        return classroomService.addPupilToClassroom(code, email);
    }
}
