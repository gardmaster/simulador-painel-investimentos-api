package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.ClienteNaoEncontradoException;

@Provider
public class ClienteNaoEncontradoExceptionMapper implements ExceptionMapper<ClienteNaoEncontradoException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ClienteNaoEncontradoException exception) {

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.NOT_FOUND.getStatusCode())
                .title("Cliente Não Encontrado")
                .detail(exception.getMessage())
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity(problemDetails)
                .build();
    }
}
