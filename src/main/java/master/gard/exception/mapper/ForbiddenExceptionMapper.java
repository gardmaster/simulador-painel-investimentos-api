package master.gard.exception.mapper;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;

@Provider
public class ForbiddenExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<ForbiddenException> {

    public ForbiddenExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(ForbiddenException exception) {

        log.warnf("Acesso negado à URI: %s",
                uriInfo != null ? uriInfo.getRequestUri().toString() : "");

        return buildResponse(
                Response.Status.FORBIDDEN,
                msg.get(MessageKeys.ACESSO_NEGADO_TITLE),
                exception.getLocalizedMessage()
        );

    }

}
