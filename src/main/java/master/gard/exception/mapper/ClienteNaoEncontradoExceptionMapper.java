package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.ClienteNaoEncontradoException;

@Provider
public class ClienteNaoEncontradoExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<ClienteNaoEncontradoException> {

    public ClienteNaoEncontradoExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(ClienteNaoEncontradoException exception) {

        log.warnf("Cliente com ID %d não encontrado", exception.getClienteId());

        return buildResponse(
                Response.Status.NOT_FOUND,
                msg.get(MessageKeys.CLIENTE_NAO_ENCONTRADO_TITLE),
                msg.format(MessageKeys.CLIENTE_NAO_ENCONTRADO_DETAIL, exception.getClienteId())
        );

    }
}
