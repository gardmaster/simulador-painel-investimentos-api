package master.gard.dto.request.simulacao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;
import master.gard.model.enums.SortDirection;
import master.gard.model.enums.sort.SimulacaoSortBy;

@Getter
@Setter
public class SimulacaoFiltroRequest {

    @QueryParam("cliente-id")
    private Long clienteId;

    @QueryParam("nome-produto")
    private String nomeProduto;

    @QueryParam("from-data-simulacao")
    private String fromDataSimulacao;

    @QueryParam("to-data-simulacao")
    private String toDataSimulacao;

    @Min(1)
    @DefaultValue("1")
    @QueryParam("page")
    private Integer page;

    @Min(1)
    @Max(1000)
    @DefaultValue("25")
    @QueryParam("pageSize")
    private Integer pageSize;

    @QueryParam("sortBy")
    private SimulacaoSortBy sortBy;

    @QueryParam("sortDirection")
    private SortDirection sortDirection;

}
