package com.devsuperior.bds04.dto;

import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.service.validation.UserUpdateValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
@UserUpdateValid
public record UserDTO(
        Long id,
        @NotBlank(message = "O primeiro nome não pode estar em branco!")
        @Size(max = 8)
        String name,
        @Email(message = "Email inválido!")
        String email,
        List<RoleDTO> roles
) {
    public static UserDTO fromUser(User user){
        List <RoleDTO> roles = new ArrayList<>();
        user.getAuthorities().forEach(a ->{
            roles.add(RoleDTO.fromRole((Role) a));
        });
        return new UserDTO(user.getId(), user.getName(), user.getEmail(),roles);
    }
}
