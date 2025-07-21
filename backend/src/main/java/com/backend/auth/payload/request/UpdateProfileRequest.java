package com.backend.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private Integer age;

    @NotBlank
    private String password;
}
