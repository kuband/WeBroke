package com.we.broke.app.payload.response;

import com.we.broke.app.model.dto.worktype.WorkTypeSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorktypeResponse {
    private List<WorkTypeSimpleDTO> workTypeSimpleDTOS;
}
