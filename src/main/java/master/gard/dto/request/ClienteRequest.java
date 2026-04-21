package master.gard.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import master.gard.model.Cliente;
import master.gard.model.enums.PerfilRisco;
import master.gard.validation.CpfCnpj;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(
        description = "Dados para cadastro ou atualização de um cliente",
        requiredProperties = {"nome", "documento", "email", "perfilRisco"}
)
public record ClienteRequest(

        @Schema(description = "Nome completo do cliente", examples = "João da Silva")
        @NotNull @NotBlank String nome,

        @Schema(description = "CPF (11 dígitos) ou CNPJ (14 caracteres)", examples = "12345678901")
        @NotNull @NotBlank @CpfCnpj String documento,

        @Schema(description = "Endereço de e-mail válido e único", examples = "joao@email.com")
        @NotNull @NotBlank @Email String email,

        @Schema(description = "Perfil de risco do investidor", examples = "CONSERVADOR",
                enumeration = {"CONSERVADOR", "MODERADO", "ARROJADO", "AGRESSIVO", "NAO_CLASSIFICADO"})
        @NotNull PerfilRisco perfilRisco

) {

    public static Cliente toEntity(ClienteRequest request) {
        var cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setDocumento(request.documento());
        cliente.setEmail(request.email());
        cliente.setPerfilRisco(request.perfilRisco());
        return cliente;
    }
}
