package com.dwes.todo_rest.repos;


import com.dwes.todo_rest.model.Task;
//import com.dwes.todo_rest.users.User;
import com.dwes.todo_rest.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAuthor(User author);

}
