package com.backend.auth.controllers;

import com.backend.auth.models.dto.UserDTO;
import com.backend.auth.models.entity.User;
import com.backend.auth.models.mapper.UserMapper;
import com.backend.auth.security.services.UserDetails;
import com.backend.auth.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private UserDetails userDetails;
    private UserMapper userMapper;

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        Optional<User> user = userDetails.getUserByID(currentUser.getId());
        return user.map(value -> ResponseEntity.ok(userMapper.sourceToDestinationAllFields(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
