package master.gard.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Dados retornados de um produto financeiro")
public record ProdutoResponse(

        @Schema(description = "Identificador único do produto", examples = "1")
        Long id,

        @Schema(description = "Nome do produto financeiro", examples = "Fundo de Investimento XYZ")
        String nome,

        @Schema(description = "Tipo do produto financeiro", examples = "CDB")
        String tipoProduto,

        @Schema(description = "Risco associado ao produto financeiro", examples = "ALTO")
        String produtoRisco,

        @Schema(description = "Rentabilidade mensal do produto financeiro", examples = "0.5")
        Double rentabilidadeMensal) {
}
