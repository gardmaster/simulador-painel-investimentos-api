package master.gard.exception;

import lombok.Getter;
import master.gard.model.enums.PerfilRisco;
import master.gard.model.enums.TipoProduto;

import java.math.BigDecimal;

@Getter
public class NenhumProdutoValidadoParaSimulacaoException extends RuntimeException {

    private final PerfilRisco perfilRiscoCliente;
    private final BigDecimal pontuacaoRiscoCliente;
    private final TipoProduto tipoProdutoSolicitado;

    public NenhumProdutoValidadoParaSimulacaoException(PerfilRisco perfilRiscoCliente, BigDecimal pontuacaoRiscoCliente,
                                                       TipoProduto tipoProdutoSolicitado) {
        this.perfilRiscoCliente = perfilRiscoCliente;
        this.pontuacaoRiscoCliente = pontuacaoRiscoCliente;
        this.tipoProdutoSolicitado = tipoProdutoSolicitado;
    }

}
