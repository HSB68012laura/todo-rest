package com.dwes.todo_rest.repos;

import com.dwes.todo_rest.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
