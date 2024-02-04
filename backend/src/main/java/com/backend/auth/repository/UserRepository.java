package com.backend.auth.repository;

import com.backend.auth.models.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = {"roles", "userOrganisations"}, type =
          EntityGraph.EntityGraphType.LOAD)
  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);
}
