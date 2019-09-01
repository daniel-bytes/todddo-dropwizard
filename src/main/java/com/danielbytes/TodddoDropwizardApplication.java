package com.danielbytes;

import com.danielbytes.core.domain.todo.*;
import com.danielbytes.core.infra.todo.TodoInMemoryRepository;
import com.danielbytes.resources.DomainExceptionMapper;
import com.danielbytes.resources.todo.TodoResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.HashMap;

public class TodddoDropwizardApplication extends Application<TodddoDropwizardConfiguration> {
    private TodoResource resource;

    public static void main(final String[] args) throws Exception {
        new TodddoDropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "todo";
    }

    @Override
    public void initialize(final Bootstrap<TodddoDropwizardConfiguration> bootstrap) {
        TodoRepository repository = new TodoInMemoryRepository(new HashMap<TodoId, Todo>() {{
            put(ImmutableTodoId.of("test"), ImmutableTodo.of(ImmutableTodoId.of("test"), "this is a test"));
        }});

        resource = new TodoResource(repository);
    }

    @Override
    public void run(final TodddoDropwizardConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(resource);
        environment.jersey().register(new DomainExceptionMapper());
    }

}
