package com.danielbytes.core.infra.todo;

import com.danielbytes.core.domain.DomainResult;
import com.danielbytes.core.domain.DomainError;
import com.danielbytes.core.domain.todo.ImmutableTodo;
import com.danielbytes.core.domain.todo.ImmutableTodoId;
import com.danielbytes.core.domain.todo.Todo;
import com.danielbytes.core.domain.todo.TodoId;
import org.junit.Test;
import org.junit.Before;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class TodoInMemoryRepositorySpec {
    private TodoId id;
    private Todo todo;
    private TodoInMemoryRepository repo;

    @Before
    public void setup() {
        id = ImmutableTodoId.of("Test");
        todo = ImmutableTodo.of(id, "testing 123");
        repo = new TodoInMemoryRepository(new HashMap<TodoId, Todo>() {{
            put(id, todo);
        }});
    }

    @Test
    public void testGetIsValid() throws Exception {
        DomainResult<Todo> result = repo.get(id).get();

        assertThat(result.isValid()).isTrue();
        assertThat(result.getResult()).isEqualTo(Optional.of(todo));
    }

    @Test
    public void testGetOfWrongIdIsInvalid() throws Exception {
        DomainResult<Todo> result = repo.get(ImmutableTodoId.of("BadId")).get();

        assertThat(result.isValid()).isFalse();
        assertThat(result.getError()).isEqualTo(Optional.of(DomainError.NotFound));
    }

    @Test
    public void testListReturnsValues() throws Exception {
        Collection<Todo> result = repo.list().get().getResult().get();

        assertThat(result).containsExactly(todo);
    }

    @Test
    public void testCreateIsValid() throws Exception {
        DomainResult<Todo> result = repo.create(
                ImmutableTodo.of(ImmutableTodoId.of(""), "created task")
        ).get();

        assertThat(result.isValid()).isTrue();
        assertThat(result.getResult().map(t -> t.id().value())).isNotEmpty();
        assertThat(result.getResult().map(Todo::task)).isEqualTo(Optional.of("created task"));
        assertThat(repo.size()).isEqualTo(2);
    }

    @Test
    public void testUpdateIsValid() throws Exception {
        Todo updated = ImmutableTodo.of(id, "updated task");

        DomainResult<Todo> result = repo.update(updated).get();

        assertThat(result.isValid()).isTrue();
        assertThat(result.getResult()).isEqualTo(Optional.of(updated));
        assertThat(repo.values().values().contains(updated)).isTrue();
    }

    @Test
    public void testUpdateOfWrongIdIsInvalid() throws Exception {
        Todo updated = ImmutableTodo.of(ImmutableTodoId.of("BadId"), "updated task");

        DomainResult<Todo> result = repo.update(updated).get();
        assertThat(result.isValid()).isFalse();
    }

    @Test
    public void testDeleteIsValid() throws Exception {
        DomainResult<Todo> result = repo.delete(id).get();

        assertThat(result.isValid()).isTrue();
        assertThat(result.getResult()).isEqualTo(Optional.of(todo));
        assertThat(repo.size()).isEqualTo(0);
    }

    @Test
    public void testDeleteOfWrongIdIsInvalid() throws Exception {
        DomainResult<Todo> result = repo.delete(ImmutableTodoId.of("BadId")).get();

        assertThat(result.isValid()).isFalse();
        assertThat(repo.size()).isEqualTo(1);
    }
}
