package com.backend.auth.controllers;

import com.backend.auth.exception.PasswordException;
import com.backend.auth.models.PasswordDto;
import com.backend.auth.models.entity.User;
import com.backend.auth.payload.response.MessageResponse;
import com.backend.auth.security.services.PasswordResetService;
import com.backend.auth.security.services.UserDetails;
import com.backend.auth.security.services.UserDetailsImpl;
import com.backend.payment.service.annotation.PremiumEndpoint;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {
	private UserDetails userDetails;
	private PasswordResetService passwordResetService;

	@GetMapping("/all")
	public MessageResponse allAccess() {
		return new MessageResponse("Public Content.");
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public MessageResponse userAccess() {
		return new MessageResponse("User Content.");
	}

	@GetMapping("/paidUser")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	@PremiumEndpoint
	public MessageResponse paidUserAccess() {
		return new MessageResponse("Paid User Content.");
	}

	@PostMapping("/userPost")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public MessageResponse userPostAccess() {
		return new MessageResponse("User Content.");
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public MessageResponse moderatorAccess() {
		return new MessageResponse("Moderator Board.");
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public MessageResponse adminAccess() {
		return new MessageResponse("Admin Board.");
	}

	@PostMapping("/user/updatePassword")
	@PreAuthorize("hasRole('USER')")
	public MessageResponse changeUserPassword(@Valid @RequestBody PasswordDto passwordDto) {
		User user = userDetails.findUserByEmail(
				((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		if (!passwordResetService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
			throw new PasswordException("Invalid old password");
		}
		passwordResetService.changeUserPassword(user, passwordDto.getNewPassword());

		return new MessageResponse("Password updated successfully!");
	}
}
