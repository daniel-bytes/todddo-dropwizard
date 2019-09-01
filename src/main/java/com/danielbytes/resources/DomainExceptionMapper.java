package com.danielbytes.resources;

import com.danielbytes.api.ApiError;
import com.danielbytes.core.domain.DomainErrorCode;
import com.danielbytes.core.domain.DomainException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {
    @Override
    public Response toResponse(DomainException exception) {
        return Response
            .status(DomainExceptionMapper.getDomainErrorHttpStatusCode(exception.getError().getErrorCode()))
            .entity(new ApiError(exception.getError()))
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build();
    }

    static int getDomainErrorHttpStatusCode(DomainErrorCode code) {
        switch (code) {
            case NOT_FOUND: return 404;
            case FAILED_VALIDATION: return 400;
            default: return 500;
        }
    }
}
