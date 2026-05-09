package master.gard.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import master.gard.model.enums.ProdutoRisco;
import master.gard.model.enums.TipoProduto;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(
        description = "Dados para cadastro ou atualização de um produto financeiro",
        requiredProperties = {"nome", "tipoProduto", "produtoRisco", "rentabilidadeMensal"}
)
public record ProdutoRequest(

        @Schema(description = "Nome do produto financeiro", examples = "Fundo de Investimento XYZ")
        @NotNull @NotBlank String nome,

        @Schema(description = "Tipo do produto financeiro", examples = "CDB",
                enumeration = {"CDB", "LCI", "LCA", "TESOURO_DIRETO_SELIC", "FIA", "FII", "CRI", "CRA", "DEBENTURE"})
        @NotNull TipoProduto tipoProduto,

        @Schema(description = "Risco associado ao produto financeiro", examples = "ALTO",
                enumeration = {"BAIXISSIMO", "BAIXO", "MEDIO", "ALTO", "ALTISSIMO"})
        @NotNull ProdutoRisco produtoRisco,

        @Schema(description = "Rentabilidade mensal do produto financeiro. Ex.: 0.01 = 1%, 0.015 = 1.5%", examples = "0.5")
        @NotNull @Positive @Digits(integer = 4, fraction = 6, message = "{validation.produto.rentabilidade.invalida}") BigDecimal rentabilidadeMensal

) {
}
