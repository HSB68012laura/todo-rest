package com.dwes.todo_rest.controller;

import com.dwes.todo_rest.model.Tag;
import com.dwes.todo_rest.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Listar todos los tags")
    @GetMapping
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    @Operation(summary = "Obtener tag por ID")
    @GetMapping("/{id}")
    public Tag findById(@PathVariable Long id) {
        return tagService.findById(id);
    }

    @Operation(summary = "Crear nuevo tag")
    @PostMapping
    public ResponseEntity<Tag> create(@RequestBody Tag tag) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.save(tag));
    }

    @Operation(summary = "Actualizar tag")
    @PutMapping("/{id}")
    public Tag update(@PathVariable Long id, @RequestBody Tag tag) {
        return tagService.update(id, tag);
    }

    @Operation(summary = "Eliminar tag")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
