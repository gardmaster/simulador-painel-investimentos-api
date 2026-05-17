package master.gard.dto.response.simulacao;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ResultadoSimulacaoResponse",
        description = "Resposta contendo o resultado da simulação, incluindo o valor final estimado, a rentabilidade efetiva e o prazo em meses.")
public record ResultadoSimulacaoResponse(

        @Schema(description = "Valor final estimado ao final do prazo da simulação", examples = "11200.00")
        BigDecimal valorFinal,

        @Schema(description = "Rentabilidade efetiva da simulacao ao final do prazo", examples = "0.12")
        BigDecimal rentabilidadeEfetiva,

        @Schema(description = "Prazo em meses da simulação", examples = "12")
        Integer prazoMeses

) {
}
