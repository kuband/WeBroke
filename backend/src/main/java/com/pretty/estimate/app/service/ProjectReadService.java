package com.we.broke.app.service;

import com.we.broke.app.model.dto.project.ProjectSimpleDTO;
import com.we.broke.app.model.entity.Project;
import com.we.broke.app.model.mapper.project.ProjectFullMapper;
import com.we.broke.app.model.mapper.project.ProjectSimpleMapper;
import com.we.broke.app.model.mapper.project.ProjectUpsertMapper;
import com.we.broke.app.repository.ProjectRepository;
import com.we.broke.auth.security.services.UserDetailsImpl;
import com.we.broke.common.model.dto.SimplePage;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ProjectReadService {

    private ProjectRepository projectRepository;
    private ProjectFullMapper projectFullMapper;
    private ProjectSimpleMapper projectSimpleMapper;
    private ProjectUpsertMapper projectUpsertMapper;
    private EntityManager entityManager;


    public List<ProjectSimpleDTO> readProjects(UserDetailsImpl userDetailsImpl) {
        return projectSimpleMapper.sourceToDestinationAllFields(getUserProjects(userDetailsImpl));
    }

    public List<Project> getUserProjects(UserDetailsImpl userDetailsImpl) {
        //filter deleted projects
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedFilter");
        filter.setParameter("isDeleted", "DELETED");

        Optional<List<Project>> byOrganisationIds =
                projectRepository.findAllByOrganisationIdIn(userDetailsImpl.getOrganisationIds().stream().toList());
        return byOrganisationIds.map(projects -> projects.stream().toList()).orElse(Collections.emptyList());
    }

    public SimplePage<ProjectSimpleDTO> readProjectsPage(UserDetailsImpl userDetailsImpl, Pageable pageable) {
        Page<Project> page = getUserProjects(userDetailsImpl, pageable);

        List<ProjectSimpleDTO> projectFullDTOS = projectSimpleMapper.sourceToDestinationAllFields(page.getContent());

        return new SimplePage<>(projectFullDTOS,
                page.getTotalElements(), pageable);
    }

    public Page<Project> getUserProjects(UserDetailsImpl userDetailsImpl, Pageable pageable) {
        //filter deleted projects
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedFilter");
        filter.setParameter("isDeleted", "DELETED");

        return projectRepository.findAllByOrganisationIdIn(userDetailsImpl.getOrganisationIds().stream().toList(), pageable);
    }
}
