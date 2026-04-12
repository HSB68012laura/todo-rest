package com.dwes.todo_rest.users;

import com.dwes.todo_rest.model.Task;
import com.dwes.todo_rest.repos.TaskRepository;
import com.dwes.todo_rest.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerCheck {

    private final TaskRepository taskRepository;

    public boolean check(Task task, Long userId) {
        if (task != null && task.getAuthor() !=null)
            return task.getAuthor().getId().equals(userId);
        return false;
    }

    public boolean check(Long taskId, Long userId) {
        return taskRepository.findById(taskId)
                .map(t->t.getAuthor().getId().equals(userId))
                .orElse(false);

    }



}
