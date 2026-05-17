package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Cliente;
import master.gard.model.Produto;
import master.gard.model.enums.PerfilRisco;
import master.gard.model.enums.TipoProduto;
import master.gard.repository.ProdutoRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class ProdutoRecomendacaoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoRecomendacaoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto recomendarProduto(Cliente cliente, TipoProduto tipoProduto) {
        PerfilRisco perfil = cliente.getPerfilRisco();
        BigDecimal faixaMin = perfil.getFaixaMin();
        BigDecimal faixaMax = perfil.getFaixaMax();

        // TIPO DO PRODUTO E O RANGE DO PERFIL (FIZ ASSIM PARA DAR MAIS IMPORTÂNCIA AO PERFIL DE RISCO)
        List<Produto> porRangePerfil = produtoRepository
                .buscarPorTipoEEntrePontuacao(tipoProduto, faixaMin, faixaMax);

        if (!porRangePerfil.isEmpty()) {
            return escolherComCriterios(porRangePerfil);
        }

        // TIPO DO PRODUTO E LIMITE MÁX DO PERFIL (SE NÃO ACHAR PRODUTOS DENTRO DO RANGE DE PERFIL)
        List<Produto> ateFaixaMax = produtoRepository
                .buscarPorTipoEAtePontuacaoMax(tipoProduto, faixaMax);

        if (!ateFaixaMax.isEmpty()) {
            return escolherComCriterios(ateFaixaMax);
        }

        // AQUI É SÓ PELO TIPO DO PRODUTO INFORMADO NA REQUISIÇÃO
        List<Produto> porTipo = produtoRepository.buscarPorTipo(tipoProduto);

        if (porTipo.isEmpty()) {
            return null;
        }

        return escolherComCriterios(porTipo);
    }

    private Produto escolherComCriterios(List<Produto> produtos) {
        Comparator<Produto> comparator = Comparator
                // MENOR RISCO PRIMEIRO
                .comparingInt((Produto p) -> p.getProdutoRisco().getPontuacao())
                // MAIOR RENTABILIDADE
                .thenComparing(Produto::getRentabilidadeMensal, Comparator.reverseOrder())
                // NOME DO PRODUTO CONTENDO "CAIXA" TEM PREFERÊNCIA MUAHAHAHA
                .thenComparing((Produto p) -> contemCaixa(p) ? 0 : 1);

        Produto melhor = produtos.stream().min(comparator).orElse(null);
        if (melhor == null) {
            return null;
        }

        List<Produto> empatados = produtos.stream()
                .filter(p -> comparator.compare(p, melhor) == 0)
                .toList();

        if (empatados.size() == 1) {
            return melhor;
        }

        // ALEATORIEDADE
        int idx = ThreadLocalRandom.current().nextInt(empatados.size());
        return empatados.get(idx);
    }

    private boolean contemCaixa(Produto produto) {
        return produto.getNome() != null
                && produto.getNome().toLowerCase().contains("caixa");
    }
}
