package master.gard.dto.request.simulacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import master.gard.model.enums.TipoProduto;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "SimulacaoRequest",
        description = "Requisição para realizar uma simulação, contendo os parâmetros necessários para calcular o resultado da simulação.",
        requiredProperties = {"valor", "prazoMeses", "tipoProduto"}
)
public record SimulacaoRequest(

        @Schema(description = "Valor solicitado para simulação", examples = "1000.50")
        @NotNull @Positive
        BigDecimal valor,

        @Schema(description = "Prazo de duração da simulação em meses", examples = "12")
        @NotNull @Positive
        Integer prazoMeses,

        @Schema(description = "Tipo do produto financeiro para o qual a simulação será realizada", examples = "CDB",
                enumeration = {"CDB", "LCI", "LCA", "FIA", "FII", "TESOURO_DIRETO_SELIC", "CRI", "CRA", "DEBENTURE"})
        @NotNull
        TipoProduto tipoProduto

) {
}
