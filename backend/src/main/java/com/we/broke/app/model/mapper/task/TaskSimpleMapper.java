package com.we.broke.app.model.mapper.task;


import com.we.broke.app.model.dto.task.TaskSimpleDTO;
import com.we.broke.app.model.entity.Task;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
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
