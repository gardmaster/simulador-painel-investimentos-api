package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.ProdutoNaoEncontradoException;

@Provider
public class ProdutoNaoEncontradoExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<ProdutoNaoEncontradoException> {

    public ProdutoNaoEncontradoExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(ProdutoNaoEncontradoException exception) {

        log.warnf("Produto com ID %d não encontrado", exception.getId());

        return buildResponse(
                Response.Status.NOT_FOUND,
                msg.get(MessageKeys.PRODUTO_NAO_ENCONTRADO_TITLE),
                msg.format(MessageKeys.PRODUTO_NAO_ENCONTRADO_DETAIL, exception.getId())
        );

    }
}
