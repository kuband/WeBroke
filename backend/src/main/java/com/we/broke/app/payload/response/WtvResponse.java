package com.we.broke.app.payload.response;

import com.we.broke.app.model.entity.WorkTypeValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WtvResponse {
    private List<WorkTypeValue> workTypeValues;
}
