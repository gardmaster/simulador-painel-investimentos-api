package master.gard.resource.produto;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.exception.ProblemDetails;
import master.gard.dto.request.ProdutoFiltroRequest;
import master.gard.dto.request.ProdutoRequest;
import master.gard.dto.response.ProdutoPageResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("api/v1/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Gerenciamento de produtos financeiros")
public interface ProdutoResourceI {

    @GET
    @RolesAllowed("admin")
    @Operation(
            summary = "Listar produtos financeiros",
            description = "Retorna uma resposta paginada com os produtos financeiros, permitindo filtros por nome, tipo, risco, rentabilidade e ordenação."
    )
    @APIResponse(
            responseCode = "200",
            description = "Produtos retornados com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProdutoPageResponse.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Parâmetros de filtro inválidos",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class)
            )
    )
    Response listarProdutos(@BeanParam ProdutoFiltroRequest filtro);

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

    // TODO: Criar o endpoint DELETE quando eu conseguir validar que o produto não possui associações com simulações ou investimentos.
}
