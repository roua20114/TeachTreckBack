package com.example.pfebackfinal.services;

import com.example.pfebackfinal.exception.ApplicationException;
import com.example.pfebackfinal.model.RegisterRequest;
import com.example.pfebackfinal.model.internal.RoleEnumeration;
import com.example.pfebackfinal.model.request.LoginRequest;
import com.example.pfebackfinal.model.response.LoginResponse;
import com.example.pfebackfinal.presistence.entity.Student;
import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.presistence.entity.UserEntity;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import com.example.pfebackfinal.security.JWTService;
import com.example.pfebackfinal.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.pfebackfinal.model.internal.RoleEnumeration.*;

/**
 * @author nidhal.ben-yarou
 */
@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(loginRequest.username());
        if(optionalUserEntity.isEmpty()){
            throw new ApplicationException("User not found with email: " + loginRequest.username());
        }
        UserEntity userEntity = optionalUserEntity.get();
        boolean PasswordMatches = passwordEncoder.matches(loginRequest.password(), userEntity.getPassword());
        if(!PasswordMatches){
            throw new ApplicationException("Invalid password");
        }
        String generatedToken = jwtService.generateToken(userEntity.getUsername());
        ApplicationUtils.setHeaderValue(HttpHeaders.AUTHORIZATION, generatedToken);
        return new LoginResponse(userEntity.getEmail(), userEntity.getRole().name(),generatedToken);
    }

    public void register(RegisterRequest registerRequest) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(registerRequest.email());
        if (existingUser.isPresent()) {
            throw new ApplicationException("User with email " + registerRequest.email() + " already exists");
        }
        UserEntity user;
        switch (registerRequest.role()) {
            case ADMIN -> user = new UserEntity();
            case STUDENT -> user = new Student();
            case TEACHER -> user = new Teacher();
            default -> throw new ApplicationException("Invalid role specified");
        }

        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEmail(registerRequest.email());
        user.setMobile(registerRequest.mobile());
        user.setAddress(registerRequest.address());
        user.setUniversity(registerRequest.university());
        user.setGender(registerRequest.gender());
        user.setRole(registerRequest.role());
        if (user instanceof Student student) {
            student.setStudyField(registerRequest.studyField());
        } else if (user instanceof Teacher teacher) {
            teacher.setDepartment(registerRequest.department());
            teacher.setDomain(registerRequest.domain());
        }

        userRepository.save(user);


    }

}
