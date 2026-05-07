package master.gard.exception.mapper;

import jakarta.ws.rs.ForbiddenException;
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
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    private static final Logger LOG = Logger.getLogger(ForbiddenExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public ForbiddenExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(ForbiddenException exception) {

        LOG.warnf("Acesso negado à URI: %s",
                uriInfo != null ? uriInfo.getRequestUri().toString() : "");

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.FORBIDDEN.getStatusCode())
                .title(msg.get(MessageKeys.ACESSO_NEGADO_TITLE))
                .detail(msg.get(MessageKeys.ACESSO_NEGADO_DETAIL))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.FORBIDDEN)
                .entity(problemDetails)
                .build();
    }

}
