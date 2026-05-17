package master.gard.dto.response.produto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ProdutoValidadoSimulacaoResponse",
        description = "Resposta contendo os detalhes do produto financeiro validado para a simulação.")
public record ProdutoValidadoSimulacaoResponse(

        @Schema(description = "ID do produto financeiro", examples = "1")
        Long id,

        @Schema(description = "Nome do produto financeiro", examples = "Fundo de Investimento XYZ")
        String nome,

        @Schema(description = "Tipo do produto financeiro", examples = "CDB",
                enumeration = {"CDB", "LCI", "LCA", "TESOURO_DIRETO_SELIC", "FIA", "FII", "CRI", "CRA", "DEBENTURE"})
        String tipoProduto,

        @Schema(description = "Rentabilidade mensal do produto financeiro. Ex.: 0.01 = 1%, 0.015 = 1.5%, 0.005 = 0.5%", examples = "0.005")
        BigDecimal rentabilidadeMensal,

        @Schema(description = "Risco associado ao produto financeiro", examples = "Alto",
                enumeration = {"Baixissimo", "Baixo", "Medio", "Alto", "Altissimo"})
        String produtoRisco

) {
}
