package com.danielbytes.api.todo;


import com.danielbytes.api.Mapper;
import com.danielbytes.core.domain.todo.ImmutableTodo;
import com.danielbytes.core.domain.todo.ImmutableTodoId;

public class TodoMappers {
    public static Mapper<Todo, com.danielbytes.core.domain.todo.Todo> apiToDomain =
        todo -> ImmutableTodo.of(
            ImmutableTodoId.of(todo.getId()),
            todo.getTask()
        );

    public static Mapper<TodoRequest, com.danielbytes.core.domain.todo.Todo> createApiToDomain =
        todo -> ImmutableTodo.of(
            ImmutableTodoId.of(""),
            todo.getTask()
        );

    public static Mapper<com.danielbytes.core.domain.todo.Todo, Todo> domainToApi =
        todo -> new Todo(
            todo.id().value(),
            todo.task()
        );
}