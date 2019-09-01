package com.danielbytes.resources.todo;

import com.codahale.metrics.annotation.Timed;
import com.danielbytes.api.todo.Todo;
import com.danielbytes.api.todo.TodoRequest;
import com.danielbytes.api.todo.TodoMappers;
import com.danielbytes.core.domain.todo.ImmutableTodoId;
import com.danielbytes.core.domain.todo.TodoRepository;
import com.danielbytes.resources.AsyncResource;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("/api/todo")
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource extends AsyncResource {
    private final TodoRepository repository;

    public TodoResource(TodoRepository repository) {
        this.repository = repository;
    }

    @GET
    @Timed
    public void listTodos(@Suspended final AsyncResponse asyncResponse) {
        this.handleCollectionResponse(
            this.repository.list(),
            TodoMappers.domainToApi,
            asyncResponse
        );
    }

    @GET
    @Path("/{id}")
    @Timed
    public void getTodo(@PathParam("id") String id, @Suspended final AsyncResponse asyncResponse) {
        this.handleResponse(
            this.repository.get(ImmutableTodoId.of(id)),
            TodoMappers.domainToApi,
            asyncResponse
        );
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public void createTodo(@Valid TodoRequest body, @Suspended final AsyncResponse asyncResponse) {
        this.handleResponse(
            this.repository.create(TodoMappers.createApiToDomain.map(body)),
            TodoMappers.domainToApi,
            asyncResponse
        );
    }

    @PUT
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateTodo(@Valid Todo body, @Suspended final AsyncResponse asyncResponse) {
        this.handleResponse(
            this.repository.update(TodoMappers.apiToDomain.map(body)),
            TodoMappers.domainToApi,
            asyncResponse
        );
    }

    @DELETE
    @Path("/{id}")
    @Timed
    public void deleteTodo(@PathParam("id") String id, @Suspended final AsyncResponse asyncResponse) {
        this.handleResponse(
            this.repository.delete(ImmutableTodoId.of(id)),
            TodoMappers.domainToApi,
            asyncResponse
        );
    }
}
