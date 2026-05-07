package master.gard.exception.mapper;

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
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GenericExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public GenericExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(Exception exception) {

        LOG.errorf(exception, "Erro interno não tratado na URI: %s",
                uriInfo != null ? uriInfo.getRequestUri().toString() : "");

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .title(msg.get(MessageKeys.ERRO_INTERNO_TITLE))
                .detail(msg.get(MessageKeys.ERRO_INTERNO_DETAIL))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(problemDetails)
                .build();
    }

}
