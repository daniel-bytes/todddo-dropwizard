package com.danielbytes.api.todo;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TodoSpec {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final Todo todo = new Todo("todo123", "Do something!");

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/todo.json"), Todo.class));

        assertThat(MAPPER.writeValueAsString(todo)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Todo todo = MAPPER.readValue(fixture("fixtures/todo.json"), Todo.class);

        final Todo expected = new Todo("todo123", "Do something!");

        assertThat(todo).isEqualToComparingFieldByField(expected);
    }
}
