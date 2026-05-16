package master.gard.exception.mapper;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.Messages;

@Provider
public class BadRequestExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<BadRequestException> {

    public BadRequestExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(BadRequestException e) {
        log.warnf("BadRequestException: %s", e.getMessage());

        return buildResponse(
                Response.Status.BAD_REQUEST,
                msg.get("error.campos-invalidos.title"),
                e.getMessage()
        );
    }
}
