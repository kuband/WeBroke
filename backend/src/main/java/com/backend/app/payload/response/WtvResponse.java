package com.backend.app.payload.response;

import com.backend.app.model.entity.WorkTypeValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WtvResponse {
    private List<WorkTypeValue> workTypeValues;
}
