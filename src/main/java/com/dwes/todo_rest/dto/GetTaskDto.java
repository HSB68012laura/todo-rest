package com.dwes.todo_rest.dto;

import com.dwes.todo_rest.model.Task;
import com.dwes.todo_rest.users.NewUserResponse;

import java.time.LocalDateTime;

public record GetTaskDto(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime deadline,
        NewUserResponse author

) {
    public static GetTaskDto of(Task t) {
        return new GetTaskDto(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getCreatedAt(),
                t.getDeadline(),
                NewUserResponse.of(t.getAuthor())
        );
    }
}
