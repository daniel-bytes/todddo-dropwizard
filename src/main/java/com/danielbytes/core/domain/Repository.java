package com.danielbytes.core.domain;

import java.util.concurrent.CompletableFuture;

public interface Repository<TID, TEntity extends Entity<TID>> {
    CompletableFuture<DomainResult<TEntity>> get(TID id);
    CompletableFuture<DomainCollectionResult<TEntity>> list();
    CompletableFuture<DomainResult<TEntity>> create(TEntity entity);
    CompletableFuture<DomainResult<TEntity>> update(TEntity entity);
    CompletableFuture<DomainResult<TEntity>> delete(TID id);
}
