package com.danielbytes.core.domain.todo;

import com.danielbytes.core.domain.DomainError;
import com.danielbytes.core.domain.Entity;
import com.danielbytes.core.domain.Validator;
import org.immutables.value.Value;

@Value.Immutable
public abstract class Todo implements Entity<TodoId> {
    @Value.Parameter
    public abstract TodoId id();

    @Value.Parameter
    public abstract String task();

    @Value.Check
    void check() {
        Validator.assertValid(
            task().length() >= 2 && task().length() <= 1000,
            DomainError.FailedValidation("Task length must be between 2 and 1000")
        );
    }
}
