package master.gard.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import master.gard.model.Produto;
import master.gard.repository.ProdutoRepository;

@ApplicationScoped
public class ProdutoService implements PanacheRepository<Produto> {

    private final ProdutoRepository produtoRepository;

    @Inject
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public List<ProdutoResponse> listarProdutos() {

    }
}
