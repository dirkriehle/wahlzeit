package org.wahlzeit_revisited.filter;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.wahlzeit_revisited.dto.ErrorDto;
import org.wahlzeit_revisited.utils.SysLog;

@Provider
public class ExceptionToResponseMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        ErrorDto errorDto;

        if (exception instanceof WebApplicationException) {
            // WebApplicationExceptions are thrown intentionally, just wrap them up in a dto
            WebApplicationException thrownException = (WebApplicationException) exception;
            statusCode = thrownException.getResponse().getStatus();
            errorDto = new ErrorDto(thrownException.getMessage());
        } else {
            // All others exceptions are not intended, log then and wrap them up.
            SysLog.logThrowable(exception);
            errorDto = new ErrorDto("Unknown error - one may look at the server logs");
        }

        return Response.status(statusCode).entity(errorDto).build();
    }
}
