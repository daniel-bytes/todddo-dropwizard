package com.danielbytes.core.domain.todo;

import static org.assertj.core.api.Assertions.*;

import com.danielbytes.core.domain.DomainException;
import org.junit.Test;

public class TodoSpec {
    private TodoId id = ImmutableTodoId.of("123");

    @Test
    public void testSucceedsIfLengthIsExactly2() {
        String value = new String(new char[2]).replace('\0', 'x');
        assertThat(ImmutableTodo.of(id, value)).isNotNull();
    }

    @Test
    public void testSucceedsIfLengthIsExactly1000() {
        String value = new String(new char[1000]).replace('\0', 'x');
        assertThat(ImmutableTodo.of(id, value)).isNotNull();
    }

    @Test
    public void testFailsIfLengthIsLessThan2() {
        String value = new String(new char[1]).replace('\0', 'x');

        assertThatThrownBy(() -> ImmutableTodo.of(id, value))
            .isInstanceOf(DomainException.class)
            .hasMessage("Task length must be between 2 and 1000");
    }

    @Test
    public void testFailsIfLengthIsGreaterThan1000() {
        String value = new String(new char[1001]).replace('\0', 'x');

        assertThatThrownBy(() -> ImmutableTodo.of(id, value))
            .isInstanceOf(DomainException.class)
            .hasMessage("Task length must be between 2 and 1000");
    }
}
