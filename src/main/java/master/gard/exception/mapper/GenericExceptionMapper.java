package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;

@Provider
public class GenericExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<Exception> {

    public GenericExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(Exception exception) {

        log.errorf(exception, "Erro interno não tratado na URI: %s",
                uriInfo != null ? uriInfo.getRequestUri().toString() : "");

        return buildResponse(
                Response.Status.INTERNAL_SERVER_ERROR,
                msg.get(MessageKeys.ERRO_INTERNO_TITLE),
                msg.get(MessageKeys.ERRO_INTERNO_DETAIL)
        );

    }

}
