package com.backend.common.websocket;

import com.backend.app.model.dto.project.ProjectSimpleDTO;
import com.backend.app.model.entity.Project;
import com.backend.app.model.mapper.project.ProjectSimpleMapper;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketListener {

    private final WebSocketController webSocketController;
    private final ProjectSimpleMapper projectSimpleMapper;

    @PostPersist
    void onPostPersist(Project project) throws Exception {
        ProjectSimpleDTO projectFullDTO = projectSimpleMapper.sourceToDestinationAllFields(project);
        webSocketController.publishProject(Collections.singletonList(projectFullDTO));
    }

    @PostUpdate
    void onPostUpdate(Project project) throws Exception {
        ProjectSimpleDTO projectFullDTO = projectSimpleMapper.sourceToDestinationAllFields(project);
        webSocketController.publishProject(Collections.singletonList(projectFullDTO));
    }
}
