package com.danielbytes.core.domain.todo;

import org.immutables.value.Value;

@Value.Immutable
public interface TodoId {
    @Value.Parameter
    String value();
}
