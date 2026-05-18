package master.gard.dto.response.cliente;

import master.gard.model.enums.PerfilRisco;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "PerfilRiscoResponse", description = "Resposta contendo informações sobre o perfil de risco de um cliente.")
public record PerfilRiscoResponse(

        @Schema(description = "ID do cliente associado ao perfil de risco.")
        Long clienteId,

        @Schema(description = "Perfil de risco do cliente, representado por um enum.")
        PerfilRisco perfil,

        @Schema(description = "Pontuação de risco do cliente")
        BigDecimal pontuacao,

        @Schema(description = "Descrição padrão do perfil de risco do cliente.")
        String descricao

) {
}
