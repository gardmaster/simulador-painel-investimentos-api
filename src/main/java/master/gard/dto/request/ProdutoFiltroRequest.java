package master.gard.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;
import master.gard.model.enums.ProdutoRisco;
import master.gard.model.enums.SortDirection;
import master.gard.model.enums.TipoProduto;
import master.gard.model.enums.sort.ProdutoSortBy;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoFiltroRequest {

    @QueryParam("nome")
    private String nome;

    @QueryParam("tipo-produto")
    private TipoProduto tipoProduto;

    @QueryParam("produto-risco")
    private ProdutoRisco produtoRisco;

    @QueryParam("rentabilidade-min")
    private BigDecimal rentabilidadeMin;

    @QueryParam("rentabilidade-max")
    private BigDecimal rentabilidadeMax;

    @Min(1)
    @DefaultValue("1")
    @QueryParam("page")
    private Integer page;

    @Min(1)
    @Max(1000)
    @DefaultValue("5")
    @QueryParam("pageSize")
    private Integer pageSize;

    @QueryParam("sortBy")
    private ProdutoSortBy sortBy;

    @QueryParam("sortDirection")
    private SortDirection sortDirection;

}
