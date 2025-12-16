package com.devsuperior.bds04.dto;

import com.devsuperior.bds04.entities.Role;

public record RoleDTO(Long id, String authority) {
    public static RoleDTO fromRole(Role role){
        return new RoleDTO(role.getId(), role.getAuthority());
    }
}
