package master.gard.dto.response.simulacao;

import master.gard.dto.response.produto.ProdutoValidadoSimulacaoResponse;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record SimulacaoSolicitadaResponse(

        @Schema(description = "Produto validado para Simulação")
        ProdutoValidadoSimulacaoResponse produtoValidado,

        @Schema(description = "Resultado da Simulação")
        ResultadoSimulacaoResponse resultadoSimulacao,

        @Schema(description = "Data/Hora da solicitação da simulação", examples = "2026-05-17T12:00:00Z")
        String dataSimulacao

) {
}
