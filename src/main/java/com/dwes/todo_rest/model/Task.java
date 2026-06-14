package com.dwes.todo_rest.model;

import com.dwes.todo_rest.model.Priority;
import com.dwes.todo_rest.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.jmx.export.annotation.ManagedAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDateTime deadline;

    @ManyToOne
    private User author;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Priority priority = Priority.BAJA;

    @Builder.Default
    private boolean completed = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;




}
