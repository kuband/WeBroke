package com.we.broke.auth.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordDto {

    private String oldPassword;

    private String token;

    @NotBlank
    @Size(max = 120)
    private String newPassword;

}
