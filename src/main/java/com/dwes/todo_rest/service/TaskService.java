package com.dwes.todo_rest.service;

import com.dwes.todo_rest.dto.EditTaskCommand;
import com.dwes.todo_rest.error.TaskNotFoundException;
import com.dwes.todo_rest.model.Category;
import com.dwes.todo_rest.model.Priority;
import com.dwes.todo_rest.model.Task;
import com.dwes.todo_rest.repos.CategoryRepository;
import com.dwes.todo_rest.repos.TagRepository;
import com.dwes.todo_rest.repos.TaskRepository;
import com.dwes.todo_rest.users.User;
import com.dwes.todo_rest.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));
    }

    public Task save(EditTaskCommand command, User author) {
        Priority priority;
        try {
            priority = Priority.valueOf(command.priority().toUpperCase());
        }
        catch (IllegalArgumentException e) {
            priority = Priority.BAJA;
        }
        Category category = null;
        if (command.categoryId() != null) {
            category = categoryRepository.findById(command.categoryId())
                    .orElseThrow(() -> new RuntimeException("No se ha encontrado la categoría"));
        }
        return taskRepository.save(
                Task.builder()
                        .title(command.title())
                        .description(command.description())
                        .deadline(command.deadline())
                        .author(author)
                        .category(category)
                        .priority(priority)
                        .build()
        );
    }

    public Task edit(EditTaskCommand cmd, Long id) {
        return taskRepository.findById(id)
                .map(t -> {
                    t.setTitle(cmd.title());
                    t.setDescription(cmd.description());
                    t.setDeadline(cmd.deadline());

                    if (cmd.priority() != null && !cmd.priority().isEmpty()){
                        try {
                            t.setPriority(Priority.valueOf(cmd.priority().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                        }
                }
                if (cmd.categoryId() != null) {
                    Category category = categoryRepository.findById(cmd.categoryId())
                            .orElseThrow(() -> new RuntimeException("No se ha encontrado la categoría"));
                    t.setCategory(category);
                }
                    if (cmd.completed() != null) {
                        t.setCompleted(cmd.completed());
                    }
                return taskRepository.save(t);
        })
            .orElseThrow(()-> new TaskNotFoundException(id));
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findByAuthor(User author) {
        return taskRepository.findByAuthorOrderByIdAsc(author);
    }

    public List<Task> findByPriority(User author, String priorityStr) {
        Priority priority;
        try {
            priority = Priority.valueOf(priorityStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Prioridad no válida. Usa: Baja, Media o Alta");
        }
        return taskRepository.findByAuthorAndPriority(author, priority);
    }

    public List<Task> findByTitle(User author, String title) {
        return taskRepository.findByAuthorAndTitleContainingIgnoreCase(author, title);
    }

    public List<Task> findByCompleted(User author, boolean completed) {
        return taskRepository.findByAuthorAndCompleted(author, completed);
    }

    public List<Task> findByCategory(User author, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return taskRepository.findByAuthorAndCategory(author, category);
    }

    public List<Task> findOverdueTasks(User author) {
        return taskRepository.findOverdueTasks(author, LocalDateTime.now());
    }

    public Task addTagToTask(Long taskId, Long tagId, User author) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!task.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta tarea");
        }

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag no encontrado"));

        task.getTags().add(tag);
        return taskRepository.save(task);
    }

    public Task removeTagFromTask(Long taskId, Long tagId, User author) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!task.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta tarea");
        }

        task.getTags().removeIf(tag -> tag.getId().equals(tagId));
        return taskRepository.save(task);
    }

    public List<Task> findTasksByTag(Long tagId, User author) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag no encontrado"));

        return taskRepository.findByAuthorOrderByIdAsc(author).stream()
                .filter(task -> task.getTags().contains(tag))
                .toList();
    }
}
