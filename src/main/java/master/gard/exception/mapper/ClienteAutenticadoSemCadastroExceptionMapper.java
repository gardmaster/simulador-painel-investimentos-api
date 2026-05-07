package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.ClienteAutenticadoSemCadastroException;

@Provider
public class ClienteAutenticadoSemCadastroExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<ClienteAutenticadoSemCadastroException> {

    public ClienteAutenticadoSemCadastroExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(ClienteAutenticadoSemCadastroException exception) {

        log.warnf("""
                Cadastro de cliente não encontrado.
                Preferred Username: %s
                AuthUserId: %s
                Nome: %s
                Email: %s
                """, exception.getUsername(), exception.getAuthUserId(), exception.getNome(), exception.getEmail());

        return buildResponse(
                Response.Status.NOT_FOUND,
                msg.get(MessageKeys.CLIENTE_AUTENTICADO_SEM_CADASTRO_TITLE),
                msg.format(MessageKeys.CLIENTE_AUTENTICADO_SEM_CADASTRO_DETAIL, exception.getUsername())
        );

    }
}
