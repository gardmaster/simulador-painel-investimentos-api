package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.EmailExistenteException;

@Provider
public class EmailExistenteExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<EmailExistenteException> {

    public EmailExistenteExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(EmailExistenteException exception) {

        log.warnf("Email duplicado encontrado: %s", exception.getEmail());

        return buildResponse(
                Response.Status.CONFLICT,
                msg.get(MessageKeys.CLIENTE_EMAIL_DUPLICADO_TITLE),
                msg.format(MessageKeys.CLIENTE_EMAIL_DUPLICADO_DETAIL, exception.getEmail())
        );

    }
}
