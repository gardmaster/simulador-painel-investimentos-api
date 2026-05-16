package master.gard.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import master.gard.dto.request.produto.ProdutoFiltroRequest;
import master.gard.dto.request.produto.ProdutoRequest;
import master.gard.dto.response.PageInfoResponse;
import master.gard.dto.response.produto.ProdutoPageResponse;
import master.gard.dto.response.produto.ProdutoResponse;
import master.gard.exception.ProdutoExistenteException;
import master.gard.exception.ProdutoNaoEncontradoException;
import master.gard.exception.RentabilidadeProdutoInvertidaException;
import master.gard.mapper.produto.ProdutoMapper;
import master.gard.model.Produto;
import master.gard.repository.ProdutoRepository;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class ProdutoService implements PanacheRepository<Produto> {

    private static final Logger LOG = Logger.getLogger(ProdutoService.class);

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    @Inject
    public ProdutoService(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    @Transactional
    public ProdutoPageResponse listarProdutos(ProdutoFiltroRequest filtro) {
        LOG.info("Listando todos os produtos financeiros");

        validarRentabilidadeFiltrada(filtro.getRentabilidadeMin(), filtro.getRentabilidadeMax());

        PanacheQuery<Produto> query = produtoRepository.buscarFiltrado(filtro);
        query.page(Page.of(filtro.getPage() - 1, filtro.getPageSize()));

        List<ProdutoResponse> produtos = produtoMapper.toResponseList(query.list());

        return new ProdutoPageResponse(
                produtos,
                new PageInfoResponse(filtro.getPage(), filtro.getPageSize(), query.count(), query.pageCount())
        );
    }

    @Transactional
    public ProdutoResponse getProdutoPorId(Long id) {
        LOG.infof("Recuperando produto financeiro com ID: %d", id);

        return produtoRepository.findByIdOptional(id)
                .map(produtoMapper::toResponse)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    @Transactional
    public ProdutoResponse cadastrarNovoProduto(ProdutoRequest request) {
        LOG.infof("Cadastrando novo produto financeiro: %s", request.nome());
        validarProdutoExistente(request.nome());

        Produto produto = produtoMapper.toEntity(request);
        produtoRepository.persist(produto);
        LOG.infof("Produto financeiro persistido com ID: %d", produto.getId());

        return produtoMapper.toResponse(produto);
    }

    @Transactional
    public ProdutoResponse atualizarProduto(Long id, ProdutoRequest request) {
        LOG.infof("Atualizando produto financeiro com ID: %d", id);

        Produto produto = produtoRepository.findByIdOptional(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        if (!produto.getNome().equals(request.nome())) {
            validarProdutoExistenteUpdate(request.nome(), id);
        }

        produtoMapper.updateEntityFromRequest(request, produto);
        produtoRepository.persist(produto);
        LOG.infof("Produto financeiro atualizado com ID: %d", produto.getId());

        return produtoMapper.toResponse(produto);
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

    private void validarRentabilidadeFiltrada(BigDecimal rentabilidadeMin, BigDecimal rentabilidadeMax) {
        LOG.infof("Validando rentabilidade filtrada: min=%s, max=%s", rentabilidadeMin, rentabilidadeMax);
        if (rentabilidadeMin != null && rentabilidadeMax != null && rentabilidadeMin.compareTo(rentabilidadeMax) > 0) {
            throw new RentabilidadeProdutoInvertidaException(rentabilidadeMin, rentabilidadeMax);
        }
    }
}
