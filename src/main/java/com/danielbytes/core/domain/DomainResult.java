package com.danielbytes.core.domain;

import java.util.Optional;
import java.util.function.Function;

public class DomainResult<TEntity extends Entity> {
    private final TEntity result;
    private final DomainError error;

    public DomainResult(TEntity entity) {
        this.result = entity;
        this.error = null;
    }

    public DomainResult(DomainError error) {
        this.result = null;
        this.error = error;
    }

    public Optional<TEntity> getResult() {
        return Optional.ofNullable(this.result);
    }

    public Optional<DomainError> getError() {
        return Optional.ofNullable(this.error);
    }

    public boolean isValid() {
        return this.error == null && this.result != null;
    }

    public DomainResult<TEntity> map(Function<TEntity, TEntity> fn) {
        return this.flatMap(e -> new DomainResult<>(fn.apply(e)));
    }

    public DomainResult<TEntity> flatMap(Function<TEntity, DomainResult<TEntity>> fn) {
        if (this.isValid()) {
            return fn.apply(this.result);
        } else {
            return this;
        }
    }
}
