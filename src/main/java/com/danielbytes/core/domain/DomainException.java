package com.danielbytes.core.domain;

/**
 * Exception type that contains a DomainError.
 * Useful in cases where a DomainError cannot be conveyed via a return value.
 */
public class DomainException extends IllegalStateException {
    private final DomainError error;

    public DomainException(DomainError error) {
        super(error.getErrorMessage());
        this.error = error;
    }

    public DomainError getError() {
        return error;
    }
}
