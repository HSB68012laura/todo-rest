package com.dwes.todo_rest.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

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
    public ResponseEntity<NewUserResponse> createUser(@RequestBody NewUserCommand cmd) {
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
        currentUser.setPassword(updatedUser.getPassword());

        User saved = userRepository.save(currentUser);
        return ResponseEntity.ok(saved);
    }

}
