package com.we.broke.common.websocket;

import com.we.broke.app.model.dto.project.ProjectSimpleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate template;

    public void publishProject(List<ProjectSimpleDTO> projectFullDTO) throws Exception {
        projectFullDTO.forEach(projectDTO1 -> {
            template.convertAndSend("/project/" + projectDTO1.getId(), projectDTO1);
        });
    }
}
