package com.dwes.todo_rest.controller;

import com.dwes.todo_rest.dto.EditTaskCommand;
import com.dwes.todo_rest.dto.GetTaskDto;
import com.dwes.todo_rest.model.Task;
import com.dwes.todo_rest.service.TaskService;
import com.dwes.todo_rest.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.dwes.todo_rest.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

@RestController
@RequestMapping ("/task")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Obtener todas las tareas del usuario",
            description = "Permite obtener todas las tareas de un usuario"
    )
    @ApiResponse(description = "Listado de tareas del usuario",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = GetTaskDto.class)),
                    examples = {
                            @ExampleObject("""
                                    [
                                        {
                                             "id": 1,
                                             "title": "Comprar alimentos",
                                             "description": "Hacer una lista de compras para el supermercado.",
                                             "createdAt": "2025-01-13T16:12:11.295172",
                                             "deadline": "2025-01-20T16:12:11.295172",
                                             "author": {
                                                 "id": 1,
                                                 "username": "pepe",
                                                 "email": "pepe@openwebinars.net"
                                             }
                                         },
                                         {
                                             "id": 51,
                                             "title": "Pagar facturas",
                                             "description": "Pagar la factura de electricidad antes de la fecha límite.",
                                             "createdAt": "2025-01-13T16:12:11.296628",
                                             "deadline": "2025-01-15T16:12:11.296628",
                                             "author": {
                                                   "id": 1,
                                                   "username": "pepe",
                                                   "email": "pepe@openwebinars.net"
                                             }
                                         }
                                    ]
                                """)
                    }
            )
    )

    @GetMapping
    public List<GetTaskDto> getAll(
            @AuthenticationPrincipal User author
    ) {
        return //taskService.findAll()
                taskService.findByAuthor(author)
                .stream()
                .map(GetTaskDto::of)
                .toList();

    }

    @Operation(
            summary = "Crear una tarea",
            description = "Permite crear una tarea asociada al usuario autenticado"
    )
    @ApiResponse(description = "Tarea recién creada",
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetTaskDto.class),
                    examples = {
                            @ExampleObject("""
                                    {
                                             "id": 1,
                                             "title": "Comprar alimentos",
                                             "description": "Hacer una lista de compras para el supermercado.",
                                             "createdAt": "2025-01-13T16:12:11.295172",
                                             "deadline": "2025-01-20T16:12:11.295172",
                                             "author": {
                                                 "id": 1,
                                                 "username": "pepe",
                                                 "email": "pepe@openwebinars.net"
                                             }
                                         }
                                """)
                    }
            )
    )

    @PostMapping
    public ResponseEntity<GetTaskDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Tarea a crear", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditTaskCommand.class),
                            examples = @ExampleObject("""
                                    {
                                         "title": "Aprender Spring Boot",
                                         "description": "Hacer todos los cursos de Spring Boot en Openwebinars.net",
                                         "deadline": "2025-12-31T23:59:59"
                                     }
                                """)
                    )
            )
            @RequestBody EditTaskCommand cmd,
            @AuthenticationPrincipal User author
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetTaskDto.of(taskService.save(cmd, author)));
    }

    @Operation(summary = "Dashboard con estadísticas de tareas")
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(@AuthenticationPrincipal User author) {
        System.out.println("=== DASHBOARD ===");
        System.out.println("Usuario: " + author.getUsername());
        List<Task> tasks = taskService.findByAuthor(author);

        long total = tasks.size();
        long completed = tasks.stream().filter(Task::isCompleted).count();
        long pending = total - completed;
        long overdue = tasks.stream()
                .filter(t -> !t.isCompleted() && t.getDeadline() != null && t.getDeadline().isBefore(LocalDateTime.now()))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTasks", total);
        stats.put("completedTasks", completed);
        stats.put("pendingTasks", pending);
        stats.put("overdueTasks", overdue);

        return ResponseEntity.ok(stats);
    }
    @GetMapping("/search")
    public List<GetTaskDto> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @AuthenticationPrincipal User author) {

        System.out.println("=== BÚSQUEDA EN REST ===");
        System.out.println("title: " + title);
        System.out.println("priority: " + priority);
        System.out.println("completed: " + completed);
        System.out.println("categoryId: " + categoryId);
        System.out.println("tagId: " + tagId);


        if (tagId != null) {
            return taskService.findTasksByTag(tagId, author).stream().map(GetTaskDto::of).toList();
        }
        if (categoryId != null) {
            return taskService.findByCategory(author, categoryId).stream().map(GetTaskDto::of).toList();
        }
        if (priority != null && !priority.isEmpty()) {
            return taskService.findByPriority(author, priority).stream().map(GetTaskDto::of).toList();
        }
        if (title != null && !title.isEmpty()) {
            return taskService.findByTitle(author, title).stream().map(GetTaskDto::of).toList();
        }
        if ("true".equals(completed)) {
            return taskService.findByCompleted(author, true).stream().map(GetTaskDto::of).toList();
        }
        if ("false".equals(completed)) {
            return taskService.findByCompleted(author, false).stream().map(GetTaskDto::of).toList();
        }
        if ("overdue".equals(completed)) {
            return taskService.findOverdueTasks(author).stream().map(GetTaskDto::of).toList();
        }

        return taskService.findByAuthor(author).stream().map(GetTaskDto::of).toList();
    }

    @Operation(summary = "Buscar tareas por tag")
    @GetMapping("/by-tag")
    public List<Task> findTasksByTag(@RequestParam Long tagId, @AuthenticationPrincipal User author) {
        return taskService.findTasksByTag(tagId, author);
    }

    @Operation(summary = "Buscar tareas por estado")
    @GetMapping("search/by-completed")
    public List<GetTaskDto> searchByCompleted(@RequestParam boolean completed, @AuthenticationPrincipal User author) {
        return taskService.findByCompleted(author, completed)
                .stream()
                .map(GetTaskDto::of)
                .toList();
    }

    @Operation(summary = "Buscar tareas por categoría")
    @GetMapping("/search/by-category")
    public List<GetTaskDto> searchByCategory(@RequestParam Long categoryId, @AuthenticationPrincipal User author) {
        return taskService.findByCategory(author, categoryId)
                .stream()
                .map(GetTaskDto::of)
                .toList();
    }

    @GetMapping("/search/by-priority")
    public List<Task> searchByPriority(@RequestParam String priority, @AuthenticationPrincipal User author) {
        return taskService.findByPriority(author, priority);
    }
    @GetMapping("/search/by-title")
    public List<Task> searchByTitle(@RequestParam String title, @AuthenticationPrincipal User author) {
        return taskService.findByTitle(author, title);
    }

    @Operation(summary = "Asignar un tag a una tarea")
    @PostMapping("/{taskId}/tags/{tagId}")
    public ResponseEntity<Task> addTagToTask(@PathVariable Long taskId, @PathVariable Long tagId, @AuthenticationPrincipal User author) {
        return ResponseEntity.ok(taskService.addTagToTask(taskId, tagId, author));
    }

    @Operation(summary = "Eliminar un tag de una tarea")
    @DeleteMapping("/{taskId}/tags/{tagId}")
    public ResponseEntity<Task> removeTagFromTask(@PathVariable Long taskId, @PathVariable Long tagId, @AuthenticationPrincipal User author) {
        return ResponseEntity.ok(taskService.removeTagFromTask(taskId, tagId, author));
    }

    @Operation(
            summary = "Editar una tarea",
            description = "Permite editar una tarea asociada al usuario autenticado si se proporciona su ID"
    )
    @ApiResponse(description = "Tarea editada",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetTaskDto.class),
                    examples = {
                            @ExampleObject("""
                                    {
                                             "id": 1,
                                             "title": "Comprar alimentos",
                                             "description": "Hacer una lista de compras para el supermercado.",
                                             "createdAt": "2025-01-13T16:12:11.295172",
                                             "deadline": "2025-01-20T16:12:11.295172",
                                             "author": {
                                                 "id": 1,
                                                 "username": "pepe",
                                                 "email": "pepe@openwebinars.net"
                                             }
                                         }
                                """)
                    }
            )
    )

    @PreAuthorize("""
            @ownerCheck.check(#id, authentication.principal.getId())
            """)
    @PutMapping("/{id}")
    public GetTaskDto edit(@RequestBody EditTaskCommand cmd,
                           @PathVariable Long id) {
        return GetTaskDto.of(taskService.edit(cmd, id));
    }

    @Operation(
            summary = "Eliminar una tarea",
            description = "Permite eliminar una tarea asociada al usuario autenticado si se proporciona su ID"
    )
    @ApiResponse(description = "Respuesta correcta de tarea eliminada",
            responseCode = "204",
            content = @Content(schema = @Schema(implementation = Void.class)))

    @PreAuthorize("""
            @ownerCheck.check(#id, authentication.principal.getId())
            """)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener una tarea concreta",
            description = "Permite obtener la una tarea concreta si se le proporciona un id"
    )
    @ApiResponse(description = "Información detallada de una tarea",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetTaskDto.class),
                    examples = {
                            @ExampleObject("""
                                    {
                                             "id": 1,
                                             "title": "Comprar alimentos",
                                             "description": "Hacer una lista de compras para el supermercado.",
                                             "createdAt": "2025-01-13T16:12:11.295172",
                                             "deadline": "2025-01-20T16:12:11.295172",
                                             "author": {
                                                 "id": 1,
                                                 "username": "pepe",
                                                 "email": "pepe@openwebinars.net"
                                             }
                                         }
                                """)
                    }
            )
    )

    @PostAuthorize("""
            returnObject.author.username == authentication.principal.username
            """)
    @GetMapping("/{id}")
    public GetTaskDto getById(@PathVariable Long id) {
        return GetTaskDto.of(taskService.findById(id));
    }

}
