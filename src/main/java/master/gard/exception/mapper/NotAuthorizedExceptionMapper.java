package master.gard.exception.mapper;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import org.jboss.logging.Logger;

@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {

    private static final Logger LOG = Logger.getLogger(NotAuthorizedExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public NotAuthorizedExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(NotAuthorizedException exception) {

        LOG.warnf("Acesso não autenticado à URI: %s",
                uriInfo != null ? uriInfo.getRequestUri().toString() : "");

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.UNAUTHORIZED.getStatusCode())
                .title(msg.get(MessageKeys.NAO_AUTENTICADO_TITLE))
                .detail(msg.get(MessageKeys.NAO_AUTENTICADO_DETAIL))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(problemDetails)
                .build();
    }
}
