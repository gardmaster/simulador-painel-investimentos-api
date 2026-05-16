package master.gard.dto.response.simulacao;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "SimulacaoResponse", description = "Resposta da simulação, contendo os detalhes do resultado da simulação.")
public record SimulacaoResponse(

        @Schema(description = "ID da simulação", examples = "1")
        Long id,

        @Schema(description = "ID do cliente associado à simulação", examples = "1")
        Long clienteId,

        @Schema(description = "Nome do produto financeiro simulado", examples = "Fundo de Investimento XYZ")
        String nomeProduto,

        @Schema(description = "Valor investido na simulação", examples = "10000.00")
        BigDecimal valorInvestido,

        @Schema(description = "Valor final estimado ao final do prazo da simulação", examples = "11000.00")
        BigDecimal valorFinal,

        @Schema(description = "Prazo da simulação em meses", examples = "12")
        Integer prazoMeses,

        @Schema(description = "Data da realização da simulação", examples = "2026-05-09T12:00:00Z")
        String dataSimulacao

) {
}
