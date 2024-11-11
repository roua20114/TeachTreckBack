package com.example.pfebackfinal.model.response;

import com.example.pfebackfinal.model.internal.RoleEnumeration;

/**
 * @author nidhal.ben-yarou
 */
public record LoginResponse(String email, String role, String token, String username, String university, String gender,  String mobile,String address,String id,String ProfilePictureUrl) {
}
