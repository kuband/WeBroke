package com.backend.auth.repository;

import com.backend.auth.models.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = {"roles", "userOrganisations"}, type =
          EntityGraph.EntityGraphType.LOAD)
  Optional<User> findByEmail(String email);

  @Override
  @EntityGraph(attributePaths = {"roles", "userOrganisations"}, type =
          EntityGraph.EntityGraphType.LOAD)
  Optional<User> findById(Long id);

  Boolean existsByEmail(String email);

  @EntityGraph(attributePaths = {"roles", "userOrganisations"}, type =
          EntityGraph.EntityGraphType.LOAD)
  List<User> findByFullNameContainingIgnoreCase(String fullName);
}
