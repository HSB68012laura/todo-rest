package com.dwes.todo_rest.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/auth/validate")
    public ResponseEntity<?> validate() {
        return ResponseEntity.ok().body(Map.of("success", true));
    }

    @Operation(
            summary = "Nuevo usuario",
            description = "Permite crear un nuevo usuario"
    )
    @ApiResponse(
            description = "El usuario se ha creado correctamente",
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NewUserResponse.class)

            )
    )
    @PostMapping("/auth/register")
    public ResponseEntity<NewUserResponse> createUser(@RequestBody @Valid NewUserCommand cmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NewUserResponse.of(userService.register(cmd)));
    }

    @Operation(summary = "Modificar perfil del usuario autenticado")
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            @RequestBody User updatedUser,
            @AuthenticationPrincipal User currentUser) {

        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        User saved = userRepository.save(currentUser);
        return ResponseEntity.ok(saved);
    }
    /*@PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> user = userRepository.findFirstByUsername(username);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "username", user.get().getUsername(),
                    "role", user.get().getRole()
            ));
        }

        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Credenciales inválidas"));
    }*/

}
