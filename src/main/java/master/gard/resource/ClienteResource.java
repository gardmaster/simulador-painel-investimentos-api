package master.gard.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.ClienteRequest;
import master.gard.service.ClienteService;

@Path("api/v1/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    private final ClienteService service;

    public ClienteResource(ClienteService service) {
        this.service = service;
    }

    @GET
    public Response listarClientes() {
        return Response.ok(service.listarClientes()).build();
    }

    @GET
    @Path("/{id}")
    public Response recuperarClientePorId(@PathParam("id") Long id) {
        return Response.ok(service.recuperarCliente(id)).build();
    }

    @POST
    public Response cadastrarCliente(@Valid @NotNull ClienteRequest request) {
        return service.cadastrarCliente(request);
    }

    @PUT
    @Path("/{id}")
    public Response atualizarCliente(@PathParam("id") Long id, @Valid @NotNull ClienteRequest request) {
        return service.atualizarCliente(id, request);
    }
}
