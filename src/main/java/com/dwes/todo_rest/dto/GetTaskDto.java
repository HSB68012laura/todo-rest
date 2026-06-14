package com.dwes.todo_rest.dto;

import com.dwes.todo_rest.model.Tag;
import com.dwes.todo_rest.model.Task;
import com.dwes.todo_rest.users.NewUserResponse;

import java.time.LocalDateTime;
import java.util.List;

/*DTO para la salida de datos de una tarea. Evita exponer la entidad Taks JPA y oculta información
sensible
 */
public record GetTaskDto(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime deadline,
        String priority,
        boolean completed,
        NewUserResponse author,
        Long categoryId,
        String categoryName,
        List<String> TagNames


        ) {
    public static GetTaskDto of(Task t) {
        return new GetTaskDto(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getCreatedAt(),
                t.getDeadline(),
                t.getPriority().name(),
                t.isCompleted(),
                NewUserResponse.of(t.getAuthor()),
                t.getCategory() != null ? t.getCategory().getId() : null,
                t.getCategory() != null ? t.getCategory().getName() : null,
                t.getTags().stream().map(Tag::getName).toList()
        );
    }
}
