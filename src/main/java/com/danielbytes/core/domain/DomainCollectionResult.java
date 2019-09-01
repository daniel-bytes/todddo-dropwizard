package com.danielbytes.core.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class DomainCollectionResult<TEntity extends Entity> {
    private final Collection<TEntity> result;
    private final DomainError error;

    public DomainCollectionResult(Collection<TEntity> entity) {
        this.result = entity;
        this.error = null;
    }

    public DomainCollectionResult(DomainError error) {
        this.result = null;
        this.error = error;
    }

    public Optional<Collection<TEntity>> getResult() {
        return Optional.ofNullable(this.result);
    }

    public Optional<DomainError> getError() {
        return Optional.ofNullable(this.error);
    }

    public boolean isValid() {
        return this.error == null && this.result != null;
    }

    public DomainCollectionResult<TEntity> map(Function<Collection<TEntity>, Collection<TEntity>> fn) {
        return this.flatMap(e -> new DomainCollectionResult<>(fn.apply(e)));
    }

    public DomainCollectionResult<TEntity> flatMap(Function<Collection<TEntity>, DomainCollectionResult<TEntity>> fn) {
        if (this.isValid()) {
            return fn.apply(this.result);
        } else {
            return this;
        }
    }
}
