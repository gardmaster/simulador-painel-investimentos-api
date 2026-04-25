package master.gard.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.ClienteRequest;
import master.gard.service.ClienteService;

public class ClienteResource implements ClienteResourceI {

    private final ClienteService service;

    @Inject
    public ClienteResource(ClienteService service) {
        this.service = service;
    }

    @Override
    public Response listarClientes() {
        return Response.ok(service.listarClientes()).build();
    }

    @Override
    public Response obterClientePorId(Long id) {
        return Response.ok(service.recuperarCliente(id)).build();
    }

    @Override
    public Response cadastrarCliente(ClienteRequest request) {
        return service.cadastrarCliente(request);
    }

    @Override
    public Response atualizarCadastroCliente(Long id, ClienteRequest request) {
        return service.atualizarCliente(id, request);
    }

    @Override
    public Response obterClienteAutenticado() {
        return service.obterClienteAutenticado();
    }

    @Override
    public Response atualizarClienteAutenticado(ClienteRequest request) {
        return service.atualizarCadastroClienteAutenticado(request);
    }

}
