package com.danielbytes.api.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class TodoRequest {
    @Length(min = 1, max = 1000)
    private String task;

    public TodoRequest() {}

    public TodoRequest(String task) {
        this.task = task;
    }

    @JsonProperty
    public String getTask() {
        return this.task;
    }
}
