package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.ProdutoExistenteException;

@Provider
public class ProdutoExistenteExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<ProdutoExistenteException> {

    public ProdutoExistenteExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(ProdutoExistenteException exception) {

        log.warnf("Produto de mesmo nome encontrado: %s", exception.getNome());

        return buildResponse(
                Response.Status.CONFLICT,
                msg.get(MessageKeys.PRODUTO_NOME_DUPLICADO_TITLE),
                msg.format(MessageKeys.PRODUTO_NOME_DUPLICADO_DETAIL, exception.getNome())
        );

    }
}
