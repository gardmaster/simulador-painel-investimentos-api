package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.ClienteAutenticadoJaCadastradoException;
import org.jboss.logging.Logger;

@Provider
public class ClienteAutenticadoJaCadastradoExceptionMapper implements ExceptionMapper<ClienteAutenticadoJaCadastradoException> {

    private static final Logger LOG = Logger.getLogger(ClienteAutenticadoJaCadastradoExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public ClienteAutenticadoJaCadastradoExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(ClienteAutenticadoJaCadastradoException exception) {

        int statusCode = Response.Status.CONFLICT.getStatusCode();

        LOG.warnf("""
                Cliente já cadastrado para AuthUserId recuperado do JWT.
                Preferred Username: %s
                AuthUserId: %s
                Nome: %s
                Email: %s
                """, exception.getUsername(), exception.getAuthUserId(), exception.getNome(), exception.getEmail());

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(statusCode)
                .title(msg.get(MessageKeys.CLIENTE_AUTENTICADO_JA_CADASTRADO_TITLE))
                .detail(msg.format(MessageKeys.CLIENTE_AUTENTICADO_JA_CADASTRADO_DETAIL, exception.getUsername()))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(statusCode)
                .entity(problemDetails)
                .build();
    }
}
