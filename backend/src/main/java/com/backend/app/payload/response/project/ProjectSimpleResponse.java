package com.backend.app.payload.response.project;

import com.backend.app.model.dto.project.ProjectSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectSimpleResponse {
    private List<ProjectSimpleDTO> projects;
}
