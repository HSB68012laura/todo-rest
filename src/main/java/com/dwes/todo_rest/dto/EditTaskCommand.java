package com.dwes.todo_rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/*DTO para la entrada de datos al crear o editar una tarea. No incluyen el ID ni el autor porque
se asignan automaticamente
 */
public record EditTaskCommand(
        String title,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime deadline,
        String priority,
        Long categoryId,
        Boolean completed,
        String tags

) {
}
