package master.gard.resource.cliente;

import jakarta.ws.rs.core.Response;
import master.gard.service.ClienteService;

public class PerfilRiscoResource implements PerfilRiscoResourceI {

    private final ClienteService clienteService;

    public PerfilRiscoResource(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public Response getPerfilRiscoByClienteId(Long clienteId) {
        return Response.ok()
                .entity(clienteService.getPerfilRiscoPorClienteId(clienteId))
                .build();
    }
}
