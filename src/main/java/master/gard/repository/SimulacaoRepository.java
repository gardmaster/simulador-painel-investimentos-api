package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.dto.request.simulacao.SimulacaoFiltroRequest;
import master.gard.model.Simulacao;
import master.gard.model.enums.SortDirection;
import master.gard.model.enums.sort.SimulacaoSortBy;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<Simulacao> {

    public PanacheQuery<Simulacao> buscarFiltrado(SimulacaoFiltroRequest filtro, Instant from, Instant to) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (filtro.getClienteId() != null) {
            query.append(" and cliente.id = :clienteId");
            params.put("clienteId", filtro.getClienteId());
        }

        if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isBlank()) {
            query.append(" and lower(produto.nome) like lower(:nomeProduto)");
            params.put("nomeProduto", "%" + filtro.getNomeProduto().trim() + "%");
        }

        if (from != null) {
            query.append(" and dataSimulacao >= :fromInstant");
            params.put("fromInstant", from);
        }

        if (to != null) {
            query.append(" and dataSimulacao < :toExclusive");
            params.put("toExclusive", to);
        }

        String campoOrdenacao = resolverCampoOrdenacao(filtro.getSortBy());
        String direcaoOrdenacao = resolverDirecao(filtro.getSortDirection());

        query.append(" order by ").append(campoOrdenacao).append(" ").append(direcaoOrdenacao);

        return find(query.toString(), params);
    }

    private String resolverCampoOrdenacao(SimulacaoSortBy sortBy) {
        if (sortBy == null) {
            return "dataSimulacao";
        }

        return switch (sortBy) {
            case ID -> "id";
            case CLIENTE_ID -> "cliente.id";
            case PRODUTO_NOME -> "produto.nome";
            case DATA_SIMULACAO -> "dataSimulacao";
        };
    }

    private String resolverDirecao(SortDirection sortDirection) {
        if (sortDirection == null) {
            return "asc";
        }
        return sortDirection.getValue();
    }

}
