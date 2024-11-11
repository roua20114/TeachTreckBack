package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Comments;
import com.example.pfebackfinal.presistence.entity.UserEntity;
import com.example.pfebackfinal.presistence.repository.CommentsRepository;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

/**
 * @author nidhal.ben-yarou
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;
    private static final String UPLOAD_DIR = "user-photos/";


    @Override
    public void deleteProfile(String id) {
        userRepository.deleteById(id);

    }
    public UserEntity findById(String id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        return userOptional.orElse(null); // Return null if not found
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
    public String saveProfilePictur(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/user-photos/" + fileName;
    }

    // Update Profile Picture URL
    public void updateProfilePictureUrl(String profilePictureUrl) {
        // Logic to update profile picture URL in the database
        // Find the user by their ID or email and update the profilePictureUrl field
    }

    // Update Other Profile Credentials
    public void updateProfile(String email, String username, String firstName, String lastName, String mobile, String address) {
        // Fetch the current user (from the session or database)
        UserEntity user = findCurrentUser();

        // Update user credentials
        user.setEmail(email);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMobile(mobile);
        user.setAddress(address);

        // Save the user with updated information
        saveUser(user);
    }

    // Delete Current Profile Picture
    public void deleteCurrentProfilePicture() {
        String profilePictureUrl = ""; // Get from the user's profile (fetch from DB)
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Path path = Paths.get(UPLOAD_DIR + profilePictureUrl);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private UserEntity findCurrentUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // Handle the case when there is no authentication or the user is not authenticated
            throw new RuntimeException("User is not authenticated");
        }

        // Assuming the user identity is either a UserDetails object or just a username (String)
        Object principal = authentication.getPrincipal();

        String username;

        if (principal instanceof UserDetails) {
            // Extract the username from the UserDetails object
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            // If the principal is a String (sometimes just the username is stored)
            username = (String) principal;
        } else {
            // Handle unexpected types of principals
            throw new RuntimeException("Unknown authentication principal type");
        }

        // Fetch the user entity from the database using the username (or email, depending on your logic)
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public UserEntity updateUserProfile(String id, UserEntity updatedUser) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user details
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setMobile(updatedUser.getMobile());

        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setUniversity(updatedUser.getUniversity());

        // Update profile picture if present
        if (updatedUser.getProfilePicture() != null) {
            existingUser.setProfilePicture(updatedUser.getProfilePicture());
            existingUser.setProfilePictureUrl(updatedUser.getProfilePictureUrl());
        }

        return userRepository.save(existingUser);
    }


    private void saveUser(UserEntity user) {
        // Save the updated user to the database
        // e.g., userRepository.save(user);
    }
    public UserEntity getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }
    public Optional<UserEntity> getUserrById(String id) {
        return userRepository.findById(id);
    }
     public boolean deleteUserById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public void saveProfilePicture(String id, MultipartFile file) throws IOException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfilePicture(file.getBytes());
        user.setProfilePictureUrl("/api/users/" + id + "/profilePicture");

        userRepository.save(user);
    }

    public byte[] getProfilePicture(String id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getProfilePicture();
    }
}
