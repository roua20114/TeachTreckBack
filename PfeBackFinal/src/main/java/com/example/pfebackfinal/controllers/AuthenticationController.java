package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.model.RegisterRequest;
import com.example.pfebackfinal.model.request.LoginRequest;
import com.example.pfebackfinal.model.response.LoginResponse;
import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.services.AuthenticationService;
import com.example.pfebackfinal.services.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/authentication-resources")
@CrossOrigin(origins="http://localhost:4200")

public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse =authenticationService.login(loginRequest);
        Map<String,Object> response=new HashMap<>();
        response.put("data",loginResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
