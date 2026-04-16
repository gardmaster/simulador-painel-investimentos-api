package master.gard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import master.gard.model.Cliente;
import master.gard.model.enums.PerfilRisco;

public record ClienteRequest(
        @NotNull @NotBlank String nome,
        @NotNull @NotBlank String documento,
        @NotNull @NotBlank String email,
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
