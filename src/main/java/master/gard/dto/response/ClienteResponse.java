package master.gard.dto.response;

import master.gard.model.Cliente;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String cpf,
        String perfilRisco
) {
    public static ClienteResponse fromEntity(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                esconderCpf(cliente.getDocumento()),
                cliente.getPerfilRisco().name()
        );
    }

    private static String esconderCpf(String cpf) {
        if (cpf != null && cpf.length() == 11) {
            String cpfFormatado = formatarCpf(cpf);
            return "***" + cpfFormatado.substring(3, 11) + "-**";
        }
        return cpf;
    }

    private static String formatarCpf(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}

