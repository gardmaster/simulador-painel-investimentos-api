package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.dto.request.produto.ProdutoFiltroRequest;
import master.gard.model.Produto;
import master.gard.model.enums.SortDirection;
import master.gard.model.enums.TipoProduto;
import master.gard.model.enums.sort.ProdutoSortBy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

    public List<Produto> buscarPorTipo(TipoProduto tipoProduto) {
        Map<String, Object> params = new HashMap<>();
        params.put("tipoProduto", tipoProduto);
        return find("tipoProduto = :tipoProduto", params).list();
    }

    public List<Produto> buscarPorTipoEEntrePontuacao(TipoProduto tipoProduto, BigDecimal pontuacaoMin, BigDecimal pontuacaoMax) {
        String jpql = """
            tipoProduto = :tipoProduto
            and (
                case produtoRisco
                    when master.gard.model.enums.ProdutoRisco.BAIXISSIMO then 10
                    when master.gard.model.enums.ProdutoRisco.BAIXO then 25
                    when master.gard.model.enums.ProdutoRisco.MEDIO then 50
                    when master.gard.model.enums.ProdutoRisco.ALTO then 76
                    else 100
                end
            ) between :pontuacaoMin and :pontuacaoMax
            """;

        Map<String, Object> params = new HashMap<>();
        params.put("tipoProduto", tipoProduto);
        params.put("pontuacaoMin", pontuacaoMin);
        params.put("pontuacaoMax", pontuacaoMax);

        return find(jpql, params).list();
    }

    public List<Produto> buscarPorTipoEAtePontuacaoMax(TipoProduto tipoProduto, BigDecimal pontuacaoMax) {
        String jpql = """
                tipoProduto = :tipoProduto
                and (
                    case produtoRisco
                        when master.gard.model.enums.ProdutoRisco.BAIXISSIMO then 10
                        when master.gard.model.enums.ProdutoRisco.BAIXO then 25
                        when master.gard.model.enums.ProdutoRisco.MEDIO then 50
                        when master.gard.model.enums.ProdutoRisco.ALTO then 76
                        else 100
                    end
                ) <= :pontuacaoMax
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("tipoProduto", tipoProduto);
        params.put("pontuacaoMax", pontuacaoMax);

        return find(jpql, params).list();
    }


    private String resolverDirecao(SortDirection sortDirection) {
        if (sortDirection == null) {
            return "desc";
        }

        return sortDirection.getValue();
    }
}
