package master.gard.resource.produto;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.ProdutoRequest;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("api/v1/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Gerenciamento de produtos financeiros")
public interface ProdutoResourceI {

    @GET
    @RolesAllowed("admin")
    @Tag(name = "Listar Produtos", description = "Retorna uma lista de todos os produtos financeiros disponíveis")
    Response listarProdutos();

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    @Tag(name = "Obter Produto por ID", description = "Retorna os detalhes de um produto financeiro específico com base no seu ID")
    Response getProdutoPorId(@PathParam("id") Long id);

    @POST
    @RolesAllowed("admin")
    @Tag(name = "Cadastrar Produto", description = "Permite o cadastro de um novo produto financeiro")
    Response cadastrarProduto(@Valid @NotNull ProdutoRequest request);

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @Tag(name = "Atualizar Produto", description = "Permite a atualização dos detalhes de um produto financeiro existente")
    Response atualizarProduto(@PathParam("id") Long id, @Valid @NotNull ProdutoRequest request);
}
