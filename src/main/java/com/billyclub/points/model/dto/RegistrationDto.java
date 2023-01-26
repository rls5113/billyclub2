package com.billyclub.points.model.dto;

import com.billyclub.points.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    public static UserEntity toUserEntity(RegistrationDto register) {
        UserEntity user = new UserEntity(register.getUsername(), register.getEmail(), register.getPassword());
        return user;
    }
}
