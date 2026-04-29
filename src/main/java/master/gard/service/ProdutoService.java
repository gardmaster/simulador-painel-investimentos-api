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

    @Transactional
    public ProdutoResponse atualizarProduto(Long id, ProdutoRequest request) {
        LOG.infof("Atualizando produto financeiro com ID: %d", id);
        Produto produto = produtoRepository.findByIdOptional(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        if (!produto.getNome().equals(request.nome())) {
            validarProdutoExistenteUpdate(request.nome(), id);
        }

        produto.setNome(request.nome());
        produto.setTipoProduto(request.tipoProduto());
        produto.setProdutoRisco(request.produtoRisco());
        produto.setRentabilidadeMensal(request.rentabilidadeMensal());
        produtoRepository.persist(produto);
        LOG.infof("Produto financeiro atualizado com ID: %d", produto.getId());

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

    private void validarProdutoExistenteUpdate(String nome, Long id) {
        LOG.infof("Validando existência de produto financeiro com nome: %s para atualização do ID: %d", nome, id);
        boolean exists = produtoRepository.find("nome", nome).firstResultOptional()
                .filter(produto -> !produto.getId().equals(id))
                .isPresent();

        if (exists) {
            LOG.warnf("Produto de mesmo nome encontrado para outro ID: %s", nome);
            throw new ProdutoExistenteException(nome);
        }
    }
}
