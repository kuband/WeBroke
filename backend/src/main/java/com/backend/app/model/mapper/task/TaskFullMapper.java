package com.backend.app.model.mapper.task;


import com.backend.app.model.dto.task.TaskFullDTO;
import com.backend.app.model.entity.Task;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskFullMapper {

    TaskFullDTO sourceToDestinationAllFields(Task source,
                                             @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Task destinationToSourceAllFields(TaskFullDTO destination,
                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<TaskFullDTO> sourceToDestinationAllFields(List<Task> source,
                                                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Task> destinationToSourceAllFields(List<TaskFullDTO> destination,
                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default TaskFullDTO sourceToDestinationAllFields(Task source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Task destinationToSourceAllFields(TaskFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<TaskFullDTO> sourceToDestinationAllFields(List<Task> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Task> destinationToSourceAllFields(List<TaskFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
