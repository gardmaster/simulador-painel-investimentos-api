package master.gard.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;
import master.gard.model.enums.ProdutoRisco;
import master.gard.model.enums.ProdutoSortBy;
import master.gard.model.enums.SortDirection;
import master.gard.model.enums.TipoProduto;

@Getter
@Setter
public class ProdutoFiltroRequest {

    @QueryParam("nome")
    private String nome;

    @QueryParam("tipoProduto")
    private TipoProduto tipoProduto;

    @QueryParam("produtoRisco")
    private ProdutoRisco produtoRisco;

    @QueryParam("rentabilidadeMin")
    private Double rentabilidadeMin;

    @QueryParam("rentabilidadeMax")
    private Double rentabilidadeMax;

    @Min(1)
    @DefaultValue("1")
    @QueryParam("page")
    private Integer page;

    @Min(1)
    @Max(1000)
    @DefaultValue("5")
    @QueryParam("pageSize")
    private Integer pageSize;

    @DefaultValue("RENTABILIDADE_MENSAL")
    @QueryParam("sortBy")
    private ProdutoSortBy sortBy;

    @DefaultValue("DESC")
    @QueryParam("sortDirection")
    private SortDirection sortDirection;

}
