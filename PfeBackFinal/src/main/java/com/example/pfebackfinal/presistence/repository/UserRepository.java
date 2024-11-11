package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.presistence.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String>{
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    UserEntity findUserByEmail(String email);

}
