package master.gard.resource.produto;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import master.gard.service.ProdutoService;

public class ProdutoResource implements ProdutoResourceI {

    private final ProdutoService service;

    @Inject
    public ProdutoResource(ProdutoService service) {
        this.service = service;
    }

    @Override
    public Response listarProdutos() {
        return Response.ok().entity(service.listarProdutos()).build();
    }

}
