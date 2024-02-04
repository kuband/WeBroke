package com.we.broke.app.model.mapper.task;


import com.we.broke.app.model.dto.task.TaskUpsertDTO;
import com.we.broke.app.model.entity.Task;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskUpsertMapper {

    TaskUpsertDTO sourceToDestinationAllFields(Task source,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "workTypeValues", ignore = true)
    void updateTaskFromDto(Task dto, @MappingTarget Task entity);

    Task destinationToSourceAllFields(TaskUpsertDTO destination,
                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<TaskUpsertDTO> sourceToDestinationAllFields(List<Task> source,
                                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Task> destinationToSourceAllFields(List<TaskUpsertDTO> destination,
                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default TaskUpsertDTO sourceToDestinationAllFields(Task source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Task destinationToSourceAllFields(TaskUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<TaskUpsertDTO> sourceToDestinationAllFields(List<Task> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Task> destinationToSourceAllFields(List<TaskUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
