package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.ClienteAutenticadoSemCadastroException;
import master.gard.exception.ClienteNaoEncontradoException;
import org.jboss.logging.Logger;

@Provider
public class ClienteAutenticadoSemCadastroExceptionMapper implements ExceptionMapper<ClienteAutenticadoSemCadastroException> {

    private static final Logger LOG = Logger.getLogger(ClienteAutenticadoSemCadastroExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public ClienteAutenticadoSemCadastroExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(ClienteAutenticadoSemCadastroException exception) {

        LOG.warnf("""
                Cadastro de cliente não encontrado.
                Preferred Username: %s
                AuthUserId: %s
                Nome: %s
                Email: %s
                """, exception.getUsername(), exception.getAuthUserId(), exception.getNome(), exception.getEmail());

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.NOT_FOUND.getStatusCode())
                .title(msg.get(MessageKeys.CLIENTE_AUTENTICADO_SEM_CADASTRO_TITLE))
                .detail(msg.format(MessageKeys.CLIENTE_AUTENTICADO_SEM_CADASTRO_DETAIL, exception.getUsername()))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity(problemDetails)
                .build();
    }
}
