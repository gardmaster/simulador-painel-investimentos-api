package master.gard.resource.produto;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.produto.ProdutoFiltroRequest;
import master.gard.dto.request.produto.ProdutoRequest;
import master.gard.dto.response.produto.ProdutoPageResponse;
import master.gard.dto.response.produto.ProdutoResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("api/v1/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos")
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
    @APIResponse(ref = "BadRequest")
    Response listarProdutos(@BeanParam ProdutoFiltroRequest filtro);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(
            summary = "Obter produto por ID",
            description = "Retorna os detalhes de um produto financeiro específico com base no seu ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "Produto encontrado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProdutoResponse.class)
            )
    )
    @APIResponse(ref = "NotFoundProduto")
    Response getProdutoPorId(
            @Parameter(description = "Identificador único do produto", required = true, example = "1")
            @PathParam("id") Long id
    );

    @POST
    @RolesAllowed("admin")
    @Operation(
            summary = "Cadastrar produto",
            description = "Permite o cadastro de um novo produto financeiro."
    )
    @APIResponse(
            responseCode = "201",
            description = "Produto cadastrado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProdutoResponse.class)
            )
    )
    @APIResponse(ref = "BadRequest")
    @APIResponse(ref = "ConflictProdutoDuplicado")
    Response cadastrarProduto(
            @RequestBody(
                    description = "Dados do produto a ser cadastrado",
                    content = @Content(schema = @Schema(implementation = ProdutoRequest.class))
            )
            @Valid @NotNull ProdutoRequest request
    );

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(
            summary = "Atualizar produto",
            description = "Permite a atualização dos detalhes de um produto financeiro existente."
    )
    @APIResponse(
            responseCode = "200",
            description = "Produto atualizado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProdutoResponse.class)
            )
    )
    @APIResponse(ref = "BadRequest")
    @APIResponse(ref = "NotFoundProduto")
    @APIResponse(ref = "ConflictProdutoDuplicado")
    Response atualizarProduto(
            @Parameter(description = "Identificador único do produto", required = true, example = "1")
            @PathParam("id") Long id,
            @RequestBody(
                    description = "Dados do produto a ser atualizado",
                    content = @Content(schema = @Schema(implementation = ProdutoRequest.class))
            )
            @Valid @NotNull ProdutoRequest request
    );

    // TODO: Criar o endpoint DELETE quando eu conseguir validar que o produto não possui associações com simulações ou investimentos.
}
