package com.dwes.todo_rest.service;

import com.dwes.todo_rest.dto.EditTaskCommand;
import com.dwes.todo_rest.error.TaskNotFoundException;
import com.dwes.todo_rest.model.Task;
import com.dwes.todo_rest.repos.TaskRepository;
import com.dwes.todo_rest.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        List<Task> result = taskRepository.findAll();

        if (result.isEmpty())
            throw new TaskNotFoundException();

        return result;
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));
    }

    public Task save(EditTaskCommand command, User author) {
        return taskRepository.save(
                Task.builder()
                        .title(command.description())
                        .description(command.description())
                        .deadline(command.deadline())
                        .author(author)
                        .build()
        );
    }

    public Task edit(EditTaskCommand cmd, Long id) {
        return taskRepository.findById(id)
                .map(t -> {
                    t.setTitle(cmd.title());
                    t.setDescription(cmd.description());
                    t.setDeadline(cmd.deadline());
                    return taskRepository.save(t);
                })
                .orElseThrow(()-> new TaskNotFoundException(id));
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findByAuthor(User author) {
        List<Task> result = taskRepository.findByAuthor(author);

        if (result.isEmpty())
            throw new TaskNotFoundException();

        return result;
    }
}
