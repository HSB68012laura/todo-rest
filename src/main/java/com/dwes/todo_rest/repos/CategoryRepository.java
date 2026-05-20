package com.dwes.todo_rest.repos;

import com.dwes.todo_rest.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
