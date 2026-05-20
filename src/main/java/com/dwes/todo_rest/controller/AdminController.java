package com.dwes.todo_rest.controller;

import com.dwes.todo_rest.users.User;
import com.dwes.todo_rest.users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @Operation(summary = "Actualizar usuario")
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        existing.setRole(user.getRole());
        return userRepository.save(existing);
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Promocionar usuario a GESTOR")
    @PostMapping("/users/{id}/promote")
    public ResponseEntity<?> promoteToGestor(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setRole("GESTOR");
        userRepository.save(user);
        return ResponseEntity.ok().body("Usuario promocionado a GESTOR");
    }

    @Operation(summary = "Degradar GESTOR a USER")
    @PostMapping("/users/{id}/demote")
    public ResponseEntity<?> demoteToUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setRole("USER");
        userRepository.save(user);
        return ResponseEntity.ok().body("Usuario degradado a USER");
    }
}
