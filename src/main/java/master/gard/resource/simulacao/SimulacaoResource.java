package master.gard.resource.simulacao;

import jakarta.ws.rs.core.Response;
import master.gard.dto.request.simulacao.SimulacaoFiltroRequest;
import master.gard.service.SimulacaoService;

public class SimulacaoResource implements SimulacaoResourceI {

    private final SimulacaoService simulacaoService;

    public SimulacaoResource(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    @Override
    public Response listarSimulacoes(SimulacaoFiltroRequest filtro) {
        return Response.ok().entity(simulacaoService.listarSimulacoes(filtro)).build();
    }
}
