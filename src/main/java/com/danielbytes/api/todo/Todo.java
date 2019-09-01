package com.danielbytes.api.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class Todo {
    @Length(min = 1, max = 64)
    private String id;

    @Length(min = 1, max = 1000)
    private String task;

    public Todo() {}

    public Todo(String id, String task) {
        this.id = id;
        this.task = task;
    }

    @JsonProperty
    public String getId() {
        return this.id;
    }

    @JsonProperty
    public String getTask() {
        return this.task;
    }
}
