package master.gard.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import master.gard.dto.request.ProdutoRequest;
import master.gard.dto.response.ProdutoResponse;
import master.gard.exception.ProdutoExistenteException;
import master.gard.exception.ProdutoNaoEncontradoException;
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

    @Transactional
    public ProdutoResponse getProdutoPorId(Long id) {
        LOG.infof("Recuperando produto financeiro com ID: %d", id);

        return produtoRepository.findByIdOptional(id)
                .map(ProdutoResponse::fromEntity)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    @Transactional
    public ProdutoResponse cadastrarNovoProduto(ProdutoRequest request) {
        LOG.infof("Cadastrando novo produto financeiro: %s", request.nome());
        validarProdutoExistente(request.nome());

        Produto produto = ProdutoRequest.toEntity(request);
        produtoRepository.persist(produto);
        LOG.infof("Produto financeiro persistido com ID: %d", produto.getId());

        return ProdutoResponse.fromEntity(produto);
    }




    private void validarProdutoExistente(String nome) {
        LOG.infof("Validando existência de produto financeiro com nome: %s", nome);
        boolean exists = produtoRepository.find("nome", nome).firstResultOptional().isPresent();
        if (exists) {
            LOG.warnf("Produto de mesmo nome encontrado: %s", nome);
            throw new ProdutoExistenteException(nome);
        }
    }
}
