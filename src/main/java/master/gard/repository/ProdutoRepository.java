package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.dto.request.ProdutoFiltroRequest;
import master.gard.model.Produto;
import master.gard.model.enums.ProdutoSortBy;
import master.gard.model.enums.SortDirection;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {

    public PanacheQuery<Produto> buscarFiltrado(ProdutoFiltroRequest filtro) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (filtro.getNome() != null && !filtro.getNome().isBlank()) {
            query.append(" and lower(nome) like lower(:nome)");
            params.put("nome", "%" + filtro.getNome().trim() + "%");
        }

        if (filtro.getTipoProduto() != null) {
            query.append(" and tipoProduto = :tipoProduto");
            params.put("tipoProduto", filtro.getTipoProduto());
        }

        if (filtro.getProdutoRisco() != null) {
            query.append(" and produtoRisco = :produtoRisco");
            params.put("produtoRisco", filtro.getProdutoRisco());
        }

        if (filtro.getRentabilidadeMin() != null) {
            query.append(" and rentabilidadeMensal >= :rentabilidadeMin");
            params.put("rentabilidadeMin", filtro.getRentabilidadeMin());
        }

        if (filtro.getRentabilidadeMax() != null) {
            query.append(" and rentabilidadeMensal <= :rentabilidadeMax");
            params.put("rentabilidadeMax", filtro.getRentabilidadeMax());
        }

        String campoOrdenacao = resolverCampoOrdenacao(filtro.getSortBy());
        String direcaoOrdenacao = resolverDirecao(filtro.getSortDirection());

        query.append(" order by ").append(campoOrdenacao).append(" ").append(direcaoOrdenacao);

        return find(query.toString(), params);
    }

    private String resolverCampoOrdenacao(ProdutoSortBy sortBy) {
        if (sortBy == null) {
            return "rentabilidadeMensal";
        }

        return switch (sortBy) {
            case ID -> "id";
            case NOME -> "nome";
            case RENTABILIDADE_MENSAL -> "rentabilidadeMensal";
            case TIPO_PRODUTO -> "tipoProduto";
            case PRODUTO_RISCO -> "produtoRisco";
            case DATA_CRIACAO -> "dataCriacao";
        };
    }

    private String resolverDirecao(SortDirection sortDirection) {
        if (sortDirection == null) {
            return "desc";
        }

        return sortDirection.getValue();
    }
}
