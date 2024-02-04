package com.backend.app.controller;

import com.backend.app.model.dto.project.ProjectSimpleDTO;
import com.backend.app.model.type.Operation;
import com.backend.app.payload.request.project.ProjectUpsertRequest;
import com.backend.app.payload.response.project.ProjectSimpleResponse;
import com.backend.app.service.ProjectModifyService;
import com.backend.app.service.ProjectReadService;
import com.backend.auth.security.services.UserDetailsImpl;
import com.backend.common.model.dto.SimplePage;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/project/")
@AllArgsConstructor
@Slf4j
public class ProjectController {

    private ProjectModifyService projectModifyService;
    private ProjectReadService projectReadService;

    @PostMapping("/upsert")
    public ResponseEntity<ProjectSimpleResponse> upsertProject(@Valid @RequestBody ProjectUpsertRequest projectRequest,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (Arrays.stream(Operation.values()).noneMatch(operation -> operation == projectRequest.getOperation())) {
            throw new UnsupportedOperationException();
        }
        return ResponseEntity.ok(projectModifyService.crudProject(projectRequest, userDetails));
    }

    @GetMapping(value = {"/get/{id}", "/get"})
    public ResponseEntity<ProjectSimpleResponse> getProjectsForUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable(value = "id",
                                                                      required = false) final String id) {
        List<ProjectSimpleDTO> projectFullDTOS = projectReadService.readProjects(userDetails);
        if (id != null) {
            projectFullDTOS =
                    projectFullDTOS.stream().filter(projectDTO -> projectDTO.getId().toString().equals(id)).collect(Collectors.toList());
        }
        return ResponseEntity.ok(new ProjectSimpleResponse(projectFullDTOS));
    }

    @GetMapping(value = {"/getPageable"})
    public ResponseEntity<SimplePage<ProjectSimpleDTO>> getProjectsForUserPaginated(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                    @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        SimplePage<ProjectSimpleDTO> projectDTOS = projectReadService.readProjectsPage(userDetails, pageable);
        return ResponseEntity.ok(projectDTOS);
    }

}
