package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.List;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.model.TodoRowMapper;

@Service
public class TodoH2Service implements TodoRepository {
   @Autowired
   private JdbcTemplate db;

   @Override
   public ArrayList<Todo> getAllTodos() {
      List<Todo> todoList = db.query("select * from TODOLIST", new TodoRowMapper());
      ArrayList<Todo> todos = new ArrayList<>(todoList);
      return todos;
   }

   @Override
   public Todo addTodo(Todo todo) {
      db.update("insert into TODOLIST(id,todo,priority,status) values(?,?,?,?) ",todo.getId(),
            todo.getTodo(), todo.getPriority(), todo.getStatus());
      Todo addedTodo = db.queryForObject("select * from TODOLIST where todo=? and priority=? and status=?",
            new TodoRowMapper(), todo.getTodo(), todo.getPriority(), todo.getStatus());
      return addedTodo;
   }

   @Override
   public Todo getTodoById(int todoId) {
      try {
         Todo todo = db.queryForObject("select * from TODOLIST where id=?", new TodoRowMapper(), todoId);
         return todo;
      } catch (Exception e) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
   }

   @Override
   public Todo updateTodo(int todoId, Todo todo) {
      if (todo.getTodo() != null) {
         db.update("update TODOLIST set todo=? where todoId=?", todo.getTodo(), todoId);
      }
      if (todo.getPriority() != null) {
         db.update("update TODOLIST set priority=? where todoId=?", todo.getPriority(), todoId);
      }
      if (todo.getStatus() != null) {
         db.update("update TODOLIST set status=? where todoId=?", todo.getStatus(), todoId);
      }
      return getTodoById(todoId);
   }

   @Override
   public void deleteTodo(int todoId) {
      db.update("delete from TODOLIST where todoId=?", todoId);
   }
}