package com.backend.auth.controllers;

import com.backend.auth.models.dto.UserDTO;
import com.backend.auth.models.dto.UserSimpleDTO;
import com.backend.auth.models.entity.User;
import com.backend.auth.models.mapper.UserMapper;
import com.backend.auth.models.mapper.UserSimpleMapper;
import com.backend.auth.repository.UserRepository;
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
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private UserDetails userDetails;
    private UserMapper userMapper;
    private PasswordResetService passwordResetService;
    private UserSimpleMapper userSimpleMapper;
    private UserRepository userRepository;

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

    @GetMapping("/users/search")
    public List<UserSimpleDTO> searchUsers(@RequestParam String name) {
        return userSimpleMapper.sourceToDestinationAllFields(
                userRepository.findByFullNameContainingIgnoreCase(name));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(userMapper.sourceToDestinationAllFields(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
