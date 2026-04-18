package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.DocumentoExistenteException;

@Provider
public class DocumentoExistenteExceptionMapper implements ExceptionMapper<DocumentoExistenteException> {

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public DocumentoExistenteExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(DocumentoExistenteException exception) {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .title(msg.get(MessageKeys.CLIENTE_DOCUMENTO_DUPLICADO_TITLE))
                .detail(msg.get(MessageKeys.CLIENTE_DOCUMENTO_DUPLICADO_DETAIL))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(problemDetails)
                .build();
    }
}
