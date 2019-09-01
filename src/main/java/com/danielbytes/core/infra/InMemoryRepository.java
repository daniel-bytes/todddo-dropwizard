package com.danielbytes.core.infra;

import com.danielbytes.core.domain.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class InMemoryRepository<TID, TEntity
        extends Entity<TID>> implements Repository<TID, TEntity> {
    private final Map<TID, TEntity> data;

    public InMemoryRepository() {
        this.data = new HashMap<>();
    }

    public InMemoryRepository(Map<TID, TEntity> data) {
        this.data = new HashMap<>(data);
    }

    public int size() {
        return this.data.size();
    }

    public Map<TID, TEntity> values() {
        return new HashMap<>(this.data);
    }

    @Override
    public CompletableFuture<DomainResult<TEntity>> get(TID id) {
        return CompletableFuture.supplyAsync(() -> {
            if (data.containsKey(id)) {
                return new DomainResult<>(data.get(id));
            } else {
                return new DomainResult<>(DomainError.NotFound);
            }
        });
    }

    @Override
    public CompletableFuture<DomainCollectionResult<TEntity>> list() {
        return CompletableFuture.supplyAsync(() ->
            new DomainCollectionResult<>(
                new ArrayList<>(data.values())
            )
        );
    }

    @Override
    public CompletableFuture<DomainResult<TEntity>> create(TEntity entity) {
        return CompletableFuture.supplyAsync(() ->
            this.createCore(entity).map(e -> {
                data.put(e.id(), e);
                return e;
            })
        );
    }

    @Override
    public CompletableFuture<DomainResult<TEntity>> update(TEntity entity) {
        return this.get(entity.id()).thenApply(result ->
            result.flatMap(e -> updateCore(e, entity).map(updated -> {
                data.put(updated.id(), updated);
                return updated;
            }))
        );
    }

    @Override
    public CompletableFuture<DomainResult<TEntity>> delete(TID id) {
        return this.get(id).thenApply(result ->
            result.map(e -> {
                data.remove(id);
                return e;
            })
        );
    }

    protected abstract DomainResult<TEntity> createCore(TEntity newEntity);

    protected abstract DomainResult<TEntity> updateCore(TEntity existingEntity, TEntity updatedEntity);
}
