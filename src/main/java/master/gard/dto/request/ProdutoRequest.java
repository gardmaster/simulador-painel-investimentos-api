package master.gard.dto.request;

import jakarta.validation.constraints.*;
import master.gard.model.Produto;
import master.gard.model.enums.ProdutoRisco;
import master.gard.model.enums.TipoProduto;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(
        description = "Dados para cadastro ou atualização de um produto financeiro",
        requiredProperties = {"nome", "tipoProduto", "produtoRisco", "rentabilidadeMensal"}
)
public record ProdutoRequest(

        @Schema(description = "Nome do produto financeiro", examples = "Fundo de Investimento XYZ")
        @NotNull @NotBlank String nome,

        @Schema(description = "Tipo do produto financeiro", examples = "Certificado de Depósito Bancário",
                enumeration = {"CDB", "LCI", "LCA", "TESOURO_DIRETO_SELIC", "FIA", "FII", "CRI", "CRA", "DEBENTURE"})
        @NotNull TipoProduto tipoProduto,

        @Schema(description = "Risco associado ao produto financeiro", examples = "Alto Risco",
                enumeration = {"BAIXISSIMO", "BAIXO", "MEDIO", "ALTO", "ALTISSIMO"})
        @NotNull ProdutoRisco produtoRisco,

        @Schema(description = "Rentabilidade mensal do produto financeiro", examples = "0.5")
        @NotNull @Positive @Digits(integer = 3, fraction = 2, message = "{validation.produto.rentabilidade.invalida}") Double rentabilidadeMensal
) {
        public static Produto toEntity(ProdutoRequest request) {
                var produto = new Produto();
                produto.setNome(request.nome());
                produto.setTipoProduto(request.tipoProduto());
                produto.setProdutoRisco(request.produtoRisco());
                produto.setRentabilidadeMensal(request.rentabilidadeMensal());
                return produto;
        }
}
