package master.gard.mapper.simulacao;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.dto.response.produto.ProdutoValidadoSimulacaoResponse;
import master.gard.dto.response.simulacao.ResultadoSimulacaoResponse;
import master.gard.dto.response.simulacao.SimulacaoSolicitadaResponse;
import master.gard.model.Produto;
import master.gard.model.Simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class SimulacaoSolicitadaMapper {

    private static final int SCALE = 2;
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ISO_INSTANT;

    public SimulacaoSolicitadaResponse toResponse(Produto produto, Simulacao simulacao) {
        ProdutoValidadoSimulacaoResponse produtoValidado = new ProdutoValidadoSimulacaoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getTipoProduto().name(),
                produto.getRentabilidadeMensal(),
                formatRisco(produto.getProdutoRisco().name())
        );

        ResultadoSimulacaoResponse resultado = new ResultadoSimulacaoResponse(
                formatDecimal(simulacao.getValorFinal()),
                simulacao.getRentabilidadeEfetiva(),
                simulacao.getPrazoMeses()
        );

        return new SimulacaoSolicitadaResponse(
                produtoValidado,
                resultado,
                ISO_FMT.format(simulacao.getDataSimulacao())
        );
    }

    private BigDecimal formatDecimal(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private String formatRisco(String riscoEnum) {
        return switch (riscoEnum) {
            case "BAIXISSIMO" -> "Baixíssimo";
            case "BAIXO" -> "Baixo";
            case "MEDIO" -> "Médio";
            case "ALTO" -> "Alto";
            case "ALTISSIMO" -> "Altíssimo";
            default -> riscoEnum;
        };
    }
}
