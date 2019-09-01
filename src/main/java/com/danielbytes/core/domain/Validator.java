package com.danielbytes.core.domain;

public class Validator {
    public static void assertValid(boolean condition, DomainError error) {
        if (!condition) {
            throw new DomainException(error);
        }
    }
}
