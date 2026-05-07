package master.gard.exception.mapper;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;

@Provider
public class NotAuthorizedExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<NotAuthorizedException> {

    public NotAuthorizedExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(NotAuthorizedException exception) {

        log.warnf("Acesso não autenticado à URI: %s",
                uriInfo != null ? uriInfo.getRequestUri().toString() : "");

        return buildResponse(
                Response.Status.UNAUTHORIZED,
                msg.get(MessageKeys.NAO_AUTENTICADO_TITLE),
                msg.get(MessageKeys.NAO_AUTENTICADO_DETAIL)
        );

    }
}
