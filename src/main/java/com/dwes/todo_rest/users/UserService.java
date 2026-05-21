package com.dwes.todo_rest.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(NewUserCommand cmd){
        User user = User.builder()
                .username(cmd.username())
                .email(cmd.email())
                .password(passwordEncoder.encode(cmd.password()))
                //.isAdmin(false) .--> Comento esta linea porque me da fallo en la tarea de DAW
                 .role("USER")  //Esta linea la añado para que no me falle el quitar la anterior
                .build();
        return userRepository.save(user);
    }
}
