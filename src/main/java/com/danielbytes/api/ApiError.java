package com.danielbytes.api;

import com.danielbytes.core.domain.DomainError;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiError {
    private final String code;
    private final String message;

    public ApiError(DomainError error) {
        this.code = error.getErrorCode().name().toLowerCase();
        this.message = error.getErrorMessage();
    }

    @JsonProperty
    public String getCode() {
        return this.code;
    }

    @JsonProperty
    public String getMessage() {
        return this.message;
    }
}
