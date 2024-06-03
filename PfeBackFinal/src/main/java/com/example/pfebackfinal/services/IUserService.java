package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Comments;
import com.example.pfebackfinal.presistence.entity.UserEntity;

public interface IUserService {
    public void deleteProfile(String id);
    public UserEntity updateProfile(UserEntity user);
    public Comments addComments(Comments comments);
    public Comments updateComments(Comments comments);
    public void deleteCommment(String commentId);
}
