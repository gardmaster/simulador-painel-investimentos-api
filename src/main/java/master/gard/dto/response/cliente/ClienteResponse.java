package master.gard.dto.response.cliente;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;


@Schema(description = "Dados retornados de um cliente")
public record ClienteResponse(
        @Schema(description = "Identificador único do cliente", examples = "1")
        Long id,

        @Schema(description = "Nome completo do cliente", examples = "João da Silva")
        String nome,

        @Schema(description = "E-mail do cliente", examples = "joao@email.com")
        String email,

        @Schema(description = "CPF ou CNPJ formatado", examples = "123.456.789-01")
        String documento,

        @Schema(description = "Perfil de risco do investidor", examples = "CONSERVADOR",
                enumeration = {"CONSERVADOR", "MODERADO", "AGRESSIVO"})
        String perfilRisco,

        @Schema(description = "Pontuação do cliente para perfil de risco, " +
                "calculada com base nas respostas do questionário de perfil de risco", examples = "12.50")
        BigDecimal pontuacaoRisco
) {

}

