package master.gard.dto.response;

import master.gard.model.Cliente;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

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

        @Schema(description = "Perfil de risco do investidor", examples = "CONSERVADOR")
        String perfilRisco
) {
    public static ClienteResponse fromEntity(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                formatarCpfCnpj(cliente.getDocumento()),
                cliente.getPerfilRisco().name()
        );
    }

    private static String formatarCpfCnpj(String documento) {
        if (documento != null && documento.length() == 11) {
            return documento.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } else if (documento != null && documento.length() == 14) {
            return documento.replaceAll("([A-Z0-9]{2})([A-Z0-9]{3})([A-Z0-9]{3})([A-Z0-9]{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        return documento;
    }
}

