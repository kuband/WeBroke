package com.backend.auth.controllers;

import com.backend.auth.models.dto.UserDTO;
import com.backend.auth.models.entity.User;
import com.backend.auth.models.mapper.UserMapper;
import com.backend.auth.exception.PasswordException;
import com.backend.auth.payload.request.UpdateProfileRequest;
import com.backend.auth.security.services.PasswordResetService;
import com.backend.auth.security.services.UserDetails;
import com.backend.auth.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private UserDetails userDetails;
    private UserMapper userMapper;
    private PasswordResetService passwordResetService;

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        Optional<User> user = userDetails.getUserByID(currentUser.getId());
        return user.map(value -> ResponseEntity.ok(userMapper.sourceToDestinationAllFields(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> updateCurrentUser(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                                    @Valid @RequestBody UpdateProfileRequest updateRequest) {
        Optional<User> optionalUser = userDetails.getUserByID(currentUser.getId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        if (!passwordResetService.checkIfValidOldPassword(user, updateRequest.getPassword())) {
            throw new PasswordException("Invalid password");
        }
        if (updateRequest.getFullName() != null) {
            user.setFullName(updateRequest.getFullName());
        }
        if (updateRequest.getAge() != null) {
            user.setAge(updateRequest.getAge());
        }
        User saved = userDetails.save(user);
        return ResponseEntity.ok(userMapper.sourceToDestinationAllFields(saved));
    }
}
