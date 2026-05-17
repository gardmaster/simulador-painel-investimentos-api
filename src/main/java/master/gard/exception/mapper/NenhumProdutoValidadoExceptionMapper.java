package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.NenhumProdutoValidadoParaSimulacaoException;

@Provider
public class NenhumProdutoValidadoExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<NenhumProdutoValidadoParaSimulacaoException> {

    public NenhumProdutoValidadoExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(NenhumProdutoValidadoParaSimulacaoException exception) {

        log.warnf(" Nenhum produto validado. perfil=%s \\ pontuacao=%.2f \\ tipoProduto=%s",
                exception.getPerfilRiscoCliente(),
                exception.getPontuacaoRiscoCliente(),
                exception.getTipoProdutoSolicitado()
        );

        return buildResponse(
                Response.Status.BAD_REQUEST,
                msg.get(MessageKeys.ERRO_PRODUTO_NAO_VALIDADO_SIMULACAO_TITLE),
                msg.format(MessageKeys.ERRO_PRODUTO_NAO_VALIDADO_SIMULACAO_DETAIL, exception.getTipoProdutoSolicitado())
        );
    }


}
