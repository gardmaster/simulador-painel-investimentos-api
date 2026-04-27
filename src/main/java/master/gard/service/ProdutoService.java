package master.gard.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import master.gard.dto.response.ProdutoResponse;
import master.gard.model.Produto;
import master.gard.repository.ProdutoRepository;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ProdutoService implements PanacheRepository<Produto> {

    private static final Logger LOG = Logger.getLogger(ProdutoService.class);

    private final ProdutoRepository produtoRepository;

    @Inject
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public List<ProdutoResponse> listarProdutos() {
        LOG.info("Listando todos os produtos financeiros");

        return produtoRepository.listAll()
                .stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
    }
}
