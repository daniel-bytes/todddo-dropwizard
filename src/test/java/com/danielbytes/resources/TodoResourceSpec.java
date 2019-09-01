package com.danielbytes.resources;

import com.danielbytes.api.todo.TodoRequest;
import com.danielbytes.core.domain.DomainCollectionResult;
import com.danielbytes.core.domain.DomainResult;
import com.danielbytes.core.domain.todo.ImmutableTodo;
import com.danielbytes.core.domain.todo.ImmutableTodoId;
import com.danielbytes.core.domain.todo.Todo;
import com.danielbytes.core.domain.todo.TodoRepository;
import com.danielbytes.resources.todo.TodoResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static io.dropwizard.testing.FixtureHelpers.*;
import static org.mockito.Mockito.*;

public class TodoResourceSpec {
    private static final ObjectMapper mapper = Jackson.newObjectMapper();
    private static final TodoRepository todoRepo = mock(TodoRepository.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory()) // for async support
            .addResource(new TodoResource(todoRepo))
            .build();

    private final Todo createTodo = ImmutableTodo.of(ImmutableTodoId.of(""), "Create something!");
    private final Todo createResultTodo = ImmutableTodo.of(ImmutableTodoId.of("new123"), "Create something!");

    private final Todo updateTodo = ImmutableTodo.of(ImmutableTodoId.of("todo123"), "Update something!");
    private final Todo updateResultTodo = ImmutableTodo.of(ImmutableTodoId.of("todo123"), "Update something!");

    private final Todo deleteResultTodo = ImmutableTodo.of(ImmutableTodoId.of("todo123"), "Delete something!");

    private final Todo getTodoResult = ImmutableTodo.of(ImmutableTodoId.of("todo123"), "Do something!");
    private final Collection<Todo> todoListResult = new ArrayList<Todo>() {{ add(getTodoResult); }};

    @Before
    public void setup() {
        when(todoRepo.list())
                .thenReturn(CompletableFuture.supplyAsync(() -> new DomainCollectionResult<>(todoListResult)));

        when(todoRepo.get(eq(getTodoResult.id())))
                .thenReturn(CompletableFuture.supplyAsync(() -> new DomainResult<>(getTodoResult)));

        when(todoRepo.create(eq(createTodo)))
            .thenReturn(CompletableFuture.supplyAsync(() -> new DomainResult<>(createResultTodo)));

        when(todoRepo.update(eq(updateTodo)))
            .thenReturn(CompletableFuture.supplyAsync(() -> new DomainResult<>(updateResultTodo)));

        when(todoRepo.delete(eq(getTodoResult.id())))
            .thenReturn(CompletableFuture.supplyAsync(() -> new DomainResult<>(deleteResultTodo)));
    }

    @After
    public void tearDown() {
        reset(todoRepo);
    }

    @Test
    public void testListTodos() throws Exception {
        Response response = resources.target("/api/todo").request().get();

        com.danielbytes.api.todo.Todo[] expected =
                mapper.readValue(fixture("fixtures/todos.json"), com.danielbytes.api.todo.Todo[].class);

        assertThat(response.getStatus()).isEqualTo(200);

        assertThat(response.readEntity(com.danielbytes.api.todo.Todo[].class))
                .usingFieldByFieldElementComparator()
                .containsSequence(expected);

        verify(todoRepo).list();
    }

    @Test
    public void testGetTodo() throws Exception {
        Response response = resources.target("/api/todo/todo123").request().get();

        com.danielbytes.api.todo.Todo expected =
                mapper.readValue(fixture("fixtures/todo.json"), com.danielbytes.api.todo.Todo.class);

        assertThat(response.getStatus()).isEqualTo(200);

        assertThat(response.readEntity(com.danielbytes.api.todo.Todo.class))
                .isEqualToComparingFieldByFieldRecursively(expected);

        verify(todoRepo).get(getTodoResult.id());
    }

    @Test
    public void testCreateTodo() throws Exception {
        Response response = resources.target("/api/todo").request().post(
            Entity.json(
                new TodoRequest("Create something!")
            )
        );

        assertThat(response.getStatus()).isEqualTo(200);

        com.danielbytes.api.todo.Todo result = response.readEntity(com.danielbytes.api.todo.Todo.class);

        assertThat(result.getId())
            .isEqualTo(createResultTodo.id().value());

        assertThat(result.getTask())
            .isEqualTo(createResultTodo.task());

        verify(todoRepo).create(createTodo);
    }

    @Test
    public void testUpdateTodo() throws Exception {
        Response response = resources.target("/api/todo").request().put(
            Entity.json(
                new com.danielbytes.api.todo.Todo("todo123", "Update something!")
            )
        );

        assertThat(response.getStatus()).isEqualTo(200);

        com.danielbytes.api.todo.Todo result = response.readEntity(com.danielbytes.api.todo.Todo.class);

        assertThat(result.getId())
            .isEqualTo(updateResultTodo.id().value());

        assertThat(result.getTask())
            .isEqualTo(updateResultTodo.task());

        verify(todoRepo).update(updateTodo);
    }

    @Test
    public void testDeleteTodo() throws Exception {
        Response response = resources.target("/api/todo/todo123").request().delete();

        assertThat(response.getStatus()).isEqualTo(200);

        com.danielbytes.api.todo.Todo result = response.readEntity(com.danielbytes.api.todo.Todo.class);

        assertThat(result.getId())
            .isEqualTo(deleteResultTodo.id().value());

        assertThat(result.getTask())
            .isEqualTo(deleteResultTodo.task());

        verify(todoRepo).delete(deleteResultTodo.id());
    }
}
