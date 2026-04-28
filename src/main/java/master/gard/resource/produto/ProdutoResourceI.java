package master.gard.resource.produto;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    Response getProdutoPorId(Long id);
}
