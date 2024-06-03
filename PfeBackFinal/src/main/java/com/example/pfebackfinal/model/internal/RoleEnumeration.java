package com.example.pfebackfinal.model.internal;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnumeration implements GrantedAuthority {
    ADMIN, TEACHER, STUDENT;


    @Override
    public String getAuthority() {
        return this.name();
    }
}
