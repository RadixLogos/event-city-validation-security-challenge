package com.devsuperior.bds04.dto;

import com.devsuperior.bds04.service.validation.UserInsertValid;

@UserInsertValid
public record UserInsertDTO(UserDTO userDTO, String password) {
}
