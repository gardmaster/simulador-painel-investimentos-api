package master.gard.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;
import master.gard.model.enums.PerfilRisco;
import master.gard.model.enums.SortDirection;
import master.gard.model.enums.sort.ClienteSortBy;

@Getter
@Setter
public class ClienteFiltroRequest {

    @QueryParam("nome")
    private String nome;

    @QueryParam("documento")
    private String documento;

    @QueryParam("email")
    private String email;

    @QueryParam("perfil-risco")
    private PerfilRisco perfilRisco;

    @Min(1)
    @DefaultValue("1")
    @QueryParam("page")
    private Integer page;

    @Min(1)
    @Max(1000)
    @DefaultValue("5")
    @QueryParam("pageSize")
    private Integer pageSize;

    @DefaultValue("NOME")
    @QueryParam("sortBy")
    private ClienteSortBy sortBy;

    @DefaultValue("DESC")
    @QueryParam("sortDirection")
    private SortDirection sortDirection;

}
