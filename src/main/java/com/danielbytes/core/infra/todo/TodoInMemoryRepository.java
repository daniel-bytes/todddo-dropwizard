package com.danielbytes.core.infra.todo;

import com.danielbytes.core.domain.DomainResult;
import com.danielbytes.core.domain.IdGenerator;
import com.danielbytes.core.domain.todo.ImmutableTodo;
import com.danielbytes.core.domain.todo.Todo;
import com.danielbytes.core.domain.todo.TodoId;
import com.danielbytes.core.domain.todo.TodoRepository;
import com.danielbytes.core.infra.InMemoryRepository;

import java.util.Map;

public class TodoInMemoryRepository extends InMemoryRepository<TodoId, Todo> implements TodoRepository {
    public TodoInMemoryRepository() { }

    public TodoInMemoryRepository(Map<TodoId, Todo> data) {
        super(data);
    }

    protected DomainResult<Todo> createCore(Todo newEntity) {
        return new DomainResult<>(
                ImmutableTodo.of(
                        IdGenerator.newTodoId(),
                        newEntity.task()
                )
        );
    }

    protected DomainResult<Todo> updateCore(Todo existingEntity, Todo updatedEntity) {
        return new DomainResult<>(updatedEntity);
    }
}
