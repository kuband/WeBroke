package com.backend.auth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String email;
    private List<String> roles;
    private List<Long> orgIds;
}
