/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com, Aron Metzig
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit_revisited.filter;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.wahlzeit_revisited.dto.ErrorDto;
import org.wahlzeit_revisited.utils.SysLog;

/*
 * Maps in internal exception to an ErrorDto
 * So outer world knows what to expect, when an error occurs
 */
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
