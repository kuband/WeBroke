package com.backend.app.payload.response;

import com.backend.app.model.dto.worktype.WorkTypeSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorktypeResponse {
    private List<WorkTypeSimpleDTO> workTypeSimpleDTOS;
}
