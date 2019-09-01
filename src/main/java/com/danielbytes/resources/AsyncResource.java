package com.danielbytes.resources;

import com.danielbytes.api.ApiError;
import com.danielbytes.api.Mapper;
import com.danielbytes.core.domain.*;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AsyncResource {
    protected <TEntity extends Entity, TApiModel> void handleCollectionResponse(
        CompletableFuture<DomainCollectionResult<TEntity>> future,
        Mapper<TEntity, TApiModel> mapper,
        final AsyncResponse response
    ) {
        future.thenAccept(result -> {
            if (result.isValid()) {
                response.resume(
                    Response.status(200)
                        .entity(result.getResult().map(r -> r.stream().map(mapper::map)))
                        .build()
                );
            } else {
                response.resume(
                    Response.status(getErrorStatusCode(result.getError()))
                        .entity(result.getError().map(ApiError::new))
                        .build()
                );
            }
        });
    }

    protected <TEntity extends Entity, TApiModel> void handleResponse(
        CompletableFuture<DomainResult<TEntity>> future,
        Mapper<TEntity, TApiModel> mapper,
        final AsyncResponse response
    ) {
        future.thenAccept(result -> {
            if (result.isValid()) {
                response.resume(
                    Response.status(200)
                        .entity(result.getResult().map(mapper::map))
                        .build()
                );
            } else {
                response.resume(
                    Response.status(getErrorStatusCode(result.getError()))
                        .entity(result.getError().map(ApiError::new))
                        .build()
                );
            }
        });
    }

    private int getErrorStatusCode(Optional<DomainError> error) {
        return error
            .map(DomainError::getErrorCode)
            .map(DomainExceptionMapper::getDomainErrorHttpStatusCode)
            .orElse(500);
    }
}
