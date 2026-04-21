package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.EmailExistenteException;

@Provider
public class EmailExistenteExceptionMapper implements ExceptionMapper<EmailExistenteException> {

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public EmailExistenteExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(EmailExistenteException exception) {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.CONFLICT.getStatusCode())
                .title(msg.get(MessageKeys.CLIENTE_EMAIL_DUPLICADO_TITLE))
                .detail(msg.get(MessageKeys.CLIENTE_EMAIL_DUPLICADO_DETAIL))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.CONFLICT)
                .entity(problemDetails)
                .build();
    }
}
