package com.backend.auth.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.backend.app.model.entity.Organisation;
import com.backend.app.model.entity.UserOrganisation;
import com.backend.auth.models.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
	@Serial
	private static final long serialVersionUID = 1L;

	private final Long id;

	private final String username;

	private final String email;

	private final boolean enabled;

	@JsonIgnore
	private String password;

	private List<Long> organisationIds;

	private final Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String email, String username, String password,
						   boolean enabled,
						   Collection<? extends GrantedAuthority> authorities,
						   List<Long> organisationIds) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.authorities = authorities;
		this.organisationIds = organisationIds;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(),
				user.getEmail(),
				user.getEmail(),
				user.getPassword(),
				user.isEnabled(),
				authorities,
				user.getUserOrganisations().stream().filter(UserOrganisation::isJoined).map(UserOrganisation::getOrganisation).map(Organisation::getId).collect(Collectors.toList()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public List<Long> getOrganisationIds() {
		return organisationIds;
	}

	public void setOrganisationIds(List<Long> organisationIds) {
		this.organisationIds = organisationIds;
	}
}
