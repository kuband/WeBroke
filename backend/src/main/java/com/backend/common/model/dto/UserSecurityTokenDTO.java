package com.backend.common.model.dto;

import com.backend.common.model.UserSecurityTokenType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSecurityTokenDTO {
    private Long id;
    private String token;
    //    private User user;
    private Long securityId;
    private Date expiryDate;
    private UserSecurityTokenType type;
}
