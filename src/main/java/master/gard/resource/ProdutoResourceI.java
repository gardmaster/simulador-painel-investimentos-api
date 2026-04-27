package master.gard.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Gerenciamento de produtos financeiros")
public interface ProdutoResourceI {

    @GET
    @RolesAllowed("admin")
    @Tag(name = "Listar Produtos", description = "Retorna uma lista de todos os produtos financeiros disponíveis")
    Response listarProdutos();

}
