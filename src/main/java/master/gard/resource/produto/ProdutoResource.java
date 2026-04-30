package master.gard.resource.produto;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.ProdutoFiltroRequest;
import master.gard.dto.request.ProdutoRequest;
import master.gard.service.ProdutoService;

public class ProdutoResource implements ProdutoResourceI {

    private final ProdutoService service;

    @Inject
    public ProdutoResource(ProdutoService service) {
        this.service = service;
    }

    @Override
    public Response listarProdutos(ProdutoFiltroRequest filtro) {
        return Response.ok().entity(service.listarProdutos(filtro)).build();
    }

    @Override
    public Response getProdutoPorId(Long id) {
        return Response.ok().entity(service.getProdutoPorId(id)).build();
    }

    @Override
    public Response cadastrarProduto(ProdutoRequest request) {
        return Response.status(Response.Status.CREATED).entity(service.cadastrarNovoProduto(request)).build();
    }

    @Override
    public Response atualizarProduto(Long id, ProdutoRequest request) {
        return Response.ok().entity(service.atualizarProduto(id, request)).build();
    }
}
