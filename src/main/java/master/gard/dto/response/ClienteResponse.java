package master.gard.dto.response;

import master.gard.model.Cliente;

public record ClienteResponse(
        String nome,
        String email,
        String documento,
        String perfilRisco
) {
    public static ClienteResponse fromEntity(Cliente cliente) {
        return new ClienteResponse(
                cliente.getNome(),
                cliente.getEmail(),
                formatarCpfCnpj(cliente.getDocumento()),
                cliente.getPerfilRisco().name()
        );
    }

    // TODO: Melhorar logica de formatação de documento e considerar cnpj alfa
    private static String formatarCpfCnpj(String documento) {
        if (documento != null && documento.length() == 11) {
            return documento.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } else if (documento != null && documento.length() == 14) {
            return documento.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        return documento;
    }
}

