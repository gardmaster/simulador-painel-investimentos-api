package master.gard.exception.mapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.ClaimSubInexistenteException;
import org.jboss.logging.Logger;


@Provider
public class ClaimSubInexistenteExceptionMapper implements ExceptionMapper<ClaimSubInexistenteException> {

    private static final Logger LOG = Logger.getLogger(ClaimSubInexistenteExceptionMapper.class);

    @Context
    private UriInfo uriInfo;

    private final Messages msg;

    @Inject
    public ClaimSubInexistenteExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(ClaimSubInexistenteException exception) {

        LOG.warnf("""
                Claim 'sub' inexistente no token JWT.
                Preferred Username: %s
                Nome: %s
                Email: %s
                """, exception.getPreferredUsername(), exception.getNome(), exception.getEmail());

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .title(msg.get(MessageKeys.CLAIM_SUB_INEXISTENTE_TITLE))
                .detail(msg.format(MessageKeys.CLAIM_SUB_INEXISTENTE_DETAIL, exception.getPreferredUsername()))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(problemDetails)
                .build();
    }
}
