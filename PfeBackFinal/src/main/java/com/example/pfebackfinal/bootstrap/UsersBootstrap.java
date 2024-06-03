//package com.example.pfebackfinal.bootstrap;
//
//import com.example.pfebackfinal.model.internal.RoleEnumeration;
//import com.example.pfebackfinal.presistence.entity.Student;
//import com.example.pfebackfinal.presistence.entity.UserEntity;
//import com.example.pfebackfinal.presistence.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
///**
// * @author nidhal.ben-yarou
// */
//@Component
//@RequiredArgsConstructor
//public class UsersBootstrap implements CommandLineRunner {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    @Override
//    public void run(String... args) {
//        userRepository.deleteAll();
//        UserEntity user = new UserEntity();
//        user.setEmail("testme@yopmail.com");
//        user.setUsername("admin");
//        user.setPassword(passwordEncoder.encode("admin123"));
//        user.setMobile("1234567890");
//        user.setGender("Male");
//        user.setAddress("Tunis");
//        user.setUniversity("ENIT");
//        user.setRole(RoleEnumeration.ADMIN);
//        userRepository.save(user);
//        Student student = new Student();
//        student.setEmail("student@yopmail.com");
//        student.setUsername("student");
//        student.setPassword(passwordEncoder.encode("student123"));
//        student.setMobile("1234567890");
//        student.setGender("Male");
//        student.setAddress("Tunis");
//        student.setUniversity("ENIT");
//        student.setRole(RoleEnumeration.STUDENT);
//        userRepository.save(student);
//    }
//}
