package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.RentabilidadeProdutoInvertidaException;

@Provider
public class RentabilidadeProdutoInvertidaExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<RentabilidadeProdutoInvertidaException> {

    public RentabilidadeProdutoInvertidaExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(RentabilidadeProdutoInvertidaException e) {

        log.warnf("Rentabilidade mínima maior que máxima: min=%s, max=%s", e.getRentabilidadeMin(), e.getRentabilidadeMax());

        return buildResponse(
                Response.Status.BAD_REQUEST,
                msg.get(MessageKeys.RENTABILIDADE_PRODUTO_INVERTIDA_TITLE),
                msg.get(MessageKeys.RENTABILIDADE_PRODUTO_INVERTIDA_DETAIL)
        );

    }
}
