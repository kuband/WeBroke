package com.backend.app.model.mapper.task;


import com.backend.app.model.dto.task.TaskSimpleDTO;
import com.backend.app.model.entity.Task;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskSimpleMapper {

    TaskSimpleDTO sourceToDestinationAllFields(Task source,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Task destinationToSourceAllFields(TaskSimpleDTO destination,
                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<TaskSimpleDTO> sourceToDestinationAllFields(List<Task> source,
                                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Task> destinationToSourceAllFields(List<TaskSimpleDTO> destination,
                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default TaskSimpleDTO sourceToDestinationAllFields(Task source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Task destinationToSourceAllFields(TaskSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<TaskSimpleDTO> sourceToDestinationAllFields(List<Task> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Task> destinationToSourceAllFields(List<TaskSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
