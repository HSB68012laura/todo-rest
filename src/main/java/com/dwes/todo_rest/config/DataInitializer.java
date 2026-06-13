package com.dwes.todo_rest.config;

import com.dwes.todo_rest.model.Category;
import com.dwes.todo_rest.model.Priority;
import com.dwes.todo_rest.model.Tag;
import com.dwes.todo_rest.model.Task;
import com.dwes.todo_rest.repos.CategoryRepository;
import com.dwes.todo_rest.repos.TagRepository;
import com.dwes.todo_rest.repos.TaskRepository;
import com.dwes.todo_rest.users.User;
import com.dwes.todo_rest.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            //Usuarios
            User laura = User.builder()
                    .username("laura")
                    .email("laura@dwes.es")
                    .password(passwordEncoder.encode("12345"))
                    .role("USER")
                    .build();

            User admin = User.builder()
                    .username("admin")
                    .email("admin@dwes.es")
                    .password(passwordEncoder.encode("12345"))
                    .role("ADMIN")
                    .build();

            User gestor = User.builder()
                    .username("gestor")
                    .email("gestor@dwes.es")
                    .password(passwordEncoder.encode("12345"))
                    .role("GESTOR")
                    .build();

            userRepository.save(laura);
            userRepository.save(admin);
            userRepository.save(gestor);

            //Categorias
            Category trabajo = Category.builder()
                    .name("Trabajo")
                    .description("Tareas de trabajo")
                    .build();
            Category personal = Category.builder()
                    .name("Personal")
                    .description("Tareas personales")
                    .build();
            Category estudio = Category.builder()
                    .name("Estudio")
                    .description("Tareas de estudios")
                    .build();

            categoryRepository.save(trabajo);
            categoryRepository.save(personal);
            categoryRepository.save(estudio);

            //Tags
            Tag urgente = Tag.builder()
                    .name("urgente")
                    .build();
            Tag importante = Tag.builder()
                    .name("importante")
                    .build();
            Tag ocio = Tag.builder()
                    .name("ocio")
                    .build();
            Tag mensa = Tag.builder()
                    .name("mensa")
                    .build();
            tagRepository.save(urgente);
            tagRepository.save(importante);
            tagRepository.save(ocio);
            tagRepository.save(mensa);

            //Tareas para usuario de prueba
            Task tarea1 = Task.builder()
                    .title("Exámenes")
                    .description("Estudiar para los exámenes")
                    .deadline(LocalDateTime.now().plusDays(2))
                    .priority(Priority.ALTA)
                    .author(laura)
                    .category(estudio)
                    .build();
            tarea1.getTags().add(urgente);
            tarea1.getTags().add(importante);

            Task tarea2 = Task.builder()
                    .title("Cena Comañeros")
                    .description("Organización cena fin de curso")
                    .deadline(LocalDateTime.now().plusDays(20))
                    .priority(Priority.MEDIA)
                    .author(laura)
                    .category(personal)
                    .build();
            tarea2.getTags().add(ocio);

            Task tarea3 =Task.builder()
                    .title("Instalar SQL")
                    .description("Instalar SQL en el servidor de pruebas")
                    .deadline(LocalDateTime.now().minusDays(17))
                    .priority(Priority.ALTA)
                    .completed(true)
                    .author(laura)
                    .category(trabajo)
                    .build();
            tarea3.getTags().add(urgente);
            tarea3.getTags().add(importante);

            Task tarea4 = Task.builder()
                    .title("Descripción RAM")
                    .description("Descripción para el formulario del festival de Benidorm")
                    .deadline(LocalDateTime.now().plusDays(3))
                    .priority(Priority.MEDIA)
                    .completed(false)
                    .author(laura)
                    .category(personal)
                    .build();
            tarea4.getTags().add(mensa);
            tarea4.getTags().add(importante);

            taskRepository.save(tarea1);
            taskRepository.save(tarea2);
            taskRepository.save(tarea3);
            taskRepository.save(tarea4);
        }
    }
}