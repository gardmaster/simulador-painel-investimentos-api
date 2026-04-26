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
        return Response.ok().entity(service.cadastrarCliente(request)).build();
    }

    @Override
    public Response atualizarCadastroCliente(Long id, ClienteRequest request) {
        return Response.ok().entity(service.atualizarCliente(id, request)).build();
    }

    @Override
    public Response obterClienteAutenticado() {
        return Response.ok().entity(service.obterClienteAutenticado()).build();
    }

    @Override
    public Response atualizarClienteAutenticado(ClienteRequest request) {
        return Response.ok().entity(service.atualizarCadastroClienteAutenticado(request)).build();
    }

}
