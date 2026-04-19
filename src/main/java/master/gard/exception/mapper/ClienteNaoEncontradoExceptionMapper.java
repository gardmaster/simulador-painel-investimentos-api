package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.ClienteNaoEncontradoException;

@Provider
public class ClienteNaoEncontradoExceptionMapper implements ExceptionMapper<ClienteNaoEncontradoException> {

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public ClienteNaoEncontradoExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(ClienteNaoEncontradoException exception) {

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.NOT_FOUND.getStatusCode())
                .title(msg.get(MessageKeys.CLIENTE_NAO_ENCONTRADO_TITLE))
                .detail(msg.format(MessageKeys.CLIENTE_NAO_ENCONTRADO_DETAIL, exception.getClienteId()))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity(problemDetails)
                .build();
    }
}
