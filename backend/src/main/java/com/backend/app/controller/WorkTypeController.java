package com.backend.app.controller;

import com.backend.app.model.type.Operation;
import com.backend.app.payload.request.WorkTypeUpsertRequest;
import com.backend.app.payload.response.WorktypeResponse;
import com.backend.app.service.WorkTypeService;
import com.backend.auth.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/workType/")
@AllArgsConstructor
@Slf4j
public class WorkTypeController {

    private WorkTypeService workTypeService;

    @PostMapping("/upsert")
    public ResponseEntity<WorktypeResponse> upsertWorktype(@Valid @RequestBody WorkTypeUpsertRequest workTypeUpsertRequest,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (Arrays.stream(Operation.values()).noneMatch(operation -> operation == workTypeUpsertRequest.getOperation())) {
            throw new UnsupportedOperationException();
        }
        return ResponseEntity.ok(workTypeService.crudWorktype(workTypeUpsertRequest, userDetails));
    }

    @GetMapping(value = {"/getByOrg/{id}", "/getByOrg"})
    public ResponseEntity<WorktypeResponse> getWorktypesByOrg(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable(value
                                                                      = "id",
                                                                      required =
                                                                              false) final String id) {
        return ResponseEntity.ok(new WorktypeResponse(workTypeService.readWorktypesByOrg(userDetails, id)));
    }

    @GetMapping(value = {"/getByProject/{id}"})
    public ResponseEntity<WorktypeResponse> getWorktypesByProject(@PathVariable(value
            = "id",
            required =
                    true) final String id) {
        return ResponseEntity.ok(new WorktypeResponse(workTypeService.readWorktypesByProject(id)));
    }

    @GetMapping(value = {"/getByEstimate/{id}"})
    public ResponseEntity<WorktypeResponse> getWorktypesByEstimate(@PathVariable(value
            = "id",
            required =
                    true) final String id) {
        return ResponseEntity.ok(new WorktypeResponse(workTypeService.readWorktypesByEstimate(id)));
    }

}
