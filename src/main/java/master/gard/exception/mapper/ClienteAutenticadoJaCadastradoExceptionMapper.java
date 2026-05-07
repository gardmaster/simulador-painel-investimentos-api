package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.ClienteAutenticadoJaCadastradoException;

@Provider
public class ClienteAutenticadoJaCadastradoExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<ClienteAutenticadoJaCadastradoException> {

    public ClienteAutenticadoJaCadastradoExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(ClienteAutenticadoJaCadastradoException exception) {

        log.warnf("""
                Cliente já cadastrado para AuthUserId recuperado do JWT.
                Preferred Username: %s
                AuthUserId: %s
                Nome: %s
                Email: %s
                """, exception.getUsername(), exception.getAuthUserId(), exception.getNome(), exception.getEmail());

        return buildResponse(
                Response.Status.CONFLICT,
                msg.get(MessageKeys.CLIENTE_AUTENTICADO_JA_CADASTRADO_TITLE),
                msg.format(MessageKeys.CLIENTE_AUTENTICADO_JA_CADASTRADO_DETAIL, exception.getUsername())
        );
    }
}
