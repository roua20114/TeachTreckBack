package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Comments;
import com.example.pfebackfinal.presistence.entity.UserEntity;
import com.example.pfebackfinal.presistence.repository.CommentsRepository;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author nidhal.ben-yarou
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;


    @Override
    public void deleteProfile(String id) {
        userRepository.deleteById(id);

    }

    @Override
    public UserEntity updateProfile(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Comments addComments(Comments comments) {
        return commentsRepository.save(comments);
    }

    @Override
    public Comments updateComments(Comments comments) {
        return commentsRepository.save(comments);
    }

    @Override
    public void deleteCommment(String commentId) {
        commentsRepository.deleteById(commentId);

    }
}
