package com.dwes.todo_rest.repos;


import com.dwes.todo_rest.model.Category;
import com.dwes.todo_rest.model.Priority;
import com.dwes.todo_rest.model.Task;
//import com.dwes.todo_rest.users.User;
import com.dwes.todo_rest.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAuthor(User author);

    List<Task> findByAuthorAndPriority(User author, Priority priority);

    List<Task>findByAuthorAndTitleContainingIgnoreCase(User author, String title);

    List<Task> findByAuthorAndCategory(User author, Category category);

    @Query("SELECT t FROM Task t WHERE t.author = :author AND t.deadline < :now AND t.completed = false")
    List<Task> findOverdueTasks(@Param("author") User author, @Param("now") LocalDateTime now);

    List<Task> findByAuthorAndCompleted(User author, boolean completed);

}
