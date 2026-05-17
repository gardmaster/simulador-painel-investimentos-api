package master.gard.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import master.gard.dto.request.simulacao.SimulacaoFiltroRequest;
import master.gard.dto.request.simulacao.SimulacaoRequest;
import master.gard.dto.response.PageInfoResponse;
import master.gard.dto.response.simulacao.SimulacaoPageResponse;
import master.gard.dto.response.simulacao.SimulacaoResponse;
import master.gard.mapper.simulacao.SimulacaoMapper;
import master.gard.model.Cliente;
import master.gard.model.Produto;
import master.gard.model.Simulacao;
import master.gard.repository.SimulacaoRepository;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    private static final Logger LOG = Logger.getLogger(SimulacaoService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo"); // escolha oficial do negócio

    private final SimulacaoRepository simulacaoRepository;
    private final SimulacaoMapper simulacaoMapper;
    private final ClienteAuthService clienteAuthService;
    private final ProdutoRecomendacaoService produtoRecomendacaoService;

    public SimulacaoService(SimulacaoRepository simulacaoRepository, SimulacaoMapper simulacaoMapper,
                            ClienteAuthService clienteAuthService, ProdutoRecomendacaoService produtoRecomendacaoService) {
        this.simulacaoRepository = simulacaoRepository;
        this.simulacaoMapper = simulacaoMapper;
        this.clienteAuthService = clienteAuthService;
        this.produtoRecomendacaoService = produtoRecomendacaoService;
    }

    @Transactional
    public SimulacaoPageResponse listarSimulacoes(SimulacaoFiltroRequest filtro) {
        LOG.infof("Listando simulações com filtros: " +
                        "clienteId=%d, nomeProduto=%s, fromDataSimulacao=%s, toDataSimulacao=%s, page=%d, pageSize=%d, sortBy=%s, sortDirection=%s",
                filtro.getClienteId(), filtro.getNomeProduto(), filtro.getFromDataSimulacao(), filtro.getToDataSimulacao(),
                filtro.getPage(), filtro.getPageSize(), filtro.getSortBy(), filtro.getSortDirection());

        LocalDate fromDataSimulacao = parseDataSimulacao(filtro.getFromDataSimulacao());
        LocalDate toDataSimulacao = parseDataSimulacao(filtro.getToDataSimulacao());
        validarDatasFiltro(fromDataSimulacao, toDataSimulacao);

        Instant fromInstant = null;
        Instant toExclusive = null;

        if (fromDataSimulacao != null) {
            fromInstant = fromDataSimulacao.atStartOfDay(ZONE).toInstant();
        }

        if (toDataSimulacao != null) {
            toExclusive = toDataSimulacao.plusDays(1).atStartOfDay(ZONE).toInstant();
        }

        PanacheQuery<Simulacao> query = simulacaoRepository.buscarFiltrado(filtro, fromInstant, toExclusive);
        query.page(Page.of(filtro.getPage() - 1, filtro.getPageSize()));

        List<SimulacaoResponse> simulacoes = simulacaoMapper.toResponseList(query.list());

        return new SimulacaoPageResponse(
                simulacoes,
                new PageInfoResponse(filtro.getPage(), filtro.getPageSize(), query.count(), query.pageCount())
        );
    }

    @Transactional
    public SimulacaoResponse simularInvestimento(SimulacaoRequest request) {

        LOG.infof("Simulando investimento com parâmetros: valor=%f, prazo=%d meses e tipoProduto=%s",
                request.valor(), request.prazoMeses(), request.tipoProduto());

        Cliente cliente = clienteAuthService.getClienteAutenticado();
        LOG.infof("Cliente autenticado encontrado: ID %d, Nome: %s", cliente.getId(), cliente.getNome());

        Produto produto = produtoRecomendacaoService.recomendarProduto(cliente, request.tipoProduto());
        if (produto == null) {
            //TODO: Criar Exception personalizada
            throw new BadRequestException("Nenhum produto encontrado para o tipo informado.");
        }

        return null;
    }


    private LocalDate parseDataSimulacao(String dataStr) {
        LOG.infof("Parseando data de simulação: %s", dataStr);

        if (dataStr == null || dataStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dataStr, DATE_FMT);
        } catch (DateTimeParseException e) {
            LOG.errorf(e, "Erro ao parsear data de simulação: %s", dataStr);
            throw new BadRequestException("Formato de data inválido. Use o formato dd-MM-yyyy.");
        }
    }

    private void validarDatasFiltro(LocalDate fromData, LocalDate toData) {
        LOG.infof("Validando datas de filtro: fromData=%s, toData=%s", fromData, toData);

        if (fromData != null && toData != null && fromData.isAfter(toData)) {
            LOG.errorf("Data 'fromDataSimulacao' (%s) é posterior a 'toDataSimulacao' (%s)", fromData, toData);
            throw new BadRequestException("'from-data-simulacao' não pode ser posterior a 'to-data-simulacao'.");
        }
    }

}
