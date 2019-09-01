package com.danielbytes.core.domain;

import com.danielbytes.core.domain.todo.ImmutableTodoId;
import com.danielbytes.core.domain.todo.TodoId;

import java.util.UUID;

public class IdGenerator {
    public static String newStringId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static TodoId newTodoId() {
        return ImmutableTodoId.of(newStringId());
    }
}
