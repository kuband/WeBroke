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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private UserDetails userDetails;
    private UserMapper userMapper;
    private PasswordResetService passwordResetService;

    private static final long MAX_PROFILE_PICTURE_SIZE = 2 * 1024 * 1024; // 2MB

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        Optional<User> user = userDetails.getUserByID(currentUser.getId());
        return user.map(value -> ResponseEntity.ok(userMapper.sourceToDestinationAllFields(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/user/picture")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> getProfilePicture(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        Optional<User> user = userDetails.getUserByID(currentUser.getId());
        if (user.isPresent() && user.get().getProfilePicture() != null) {
            HttpHeaders headers = new HttpHeaders();
            String type = user.get().getProfilePictureType();
            MediaType mediaType = type != null ? MediaType.parseMediaType(type) : MediaType.APPLICATION_OCTET_STREAM;
            headers.setContentType(mediaType);
            return new ResponseEntity<>(user.get().getProfilePicture(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/user/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> uploadProfilePicture(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() > MAX_PROFILE_PICTURE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        }
        Optional<User> user = userDetails.getUserByID(currentUser.getId());
        if (user.isPresent()) {
            User u = user.get();
            u.setProfilePicture(file.getBytes());
            u.setProfilePictureType(file.getContentType());
            userDetails.save(u);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
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
