package com.danielbytes.core.domain;

public class DomainError {
    private final DomainErrorCode errorCode;
    private final String errorMessage;

    public DomainError(DomainErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public DomainErrorCode getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public static DomainError NotFound =
        new DomainError(
            DomainErrorCode.NOT_FOUND,
            "The entity was not found"
        );

    public static DomainError FailedValidation(String message) {
        return new DomainError(
            DomainErrorCode.FAILED_VALIDATION,
            message
        );
    }
}
