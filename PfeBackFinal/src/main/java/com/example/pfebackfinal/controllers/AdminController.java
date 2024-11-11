package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.presistence.entity.Student;
import com.example.pfebackfinal.presistence.entity.UserEntity;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nidhal.ben-yarou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/private/admin-resources")

@CrossOrigin(origins="http://localhost:4200")
public class AdminController {
    private final UserRepository userRepository;

    @GetMapping("/students")
    public ResponseEntity<List<UserEntity>> users() {
        List<UserEntity> users = userRepository.findAll();
        users = users.stream().filter(Student.class::isInstance).toList();
        return ResponseEntity.ok(users);
    }

}
