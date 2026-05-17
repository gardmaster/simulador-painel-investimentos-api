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
import master.gard.dto.response.simulacao.SimulacaoSolicitadaResponse;
import master.gard.exception.NenhumProdutoValidadoParaSimulacaoException;
import master.gard.mapper.simulacao.SimulacaoMapper;
import master.gard.mapper.simulacao.SimulacaoSolicitadaMapper;
import master.gard.model.Cliente;
import master.gard.model.Investimento;
import master.gard.model.Produto;
import master.gard.model.Simulacao;
import master.gard.repository.SimulacaoRepository;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    private static final Logger LOG = Logger.getLogger(SimulacaoService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo"); // escolha oficial do negócio

    private final SimulacaoRepository simulacaoRepository;
    private final SimulacaoCalculoService simulacaoCalculoService;
    private final SimulacaoMapper simulacaoMapper;
    private final ClienteAuthService clienteAuthService;
    private final ProdutoRecomendacaoService produtoRecomendacaoService;
    private final SimulacaoSolicitadaMapper simulacaoSolicitadaMapper;
    private final PerfilRiscoRecalculoService perfilRiscoRecalculoService;

    public SimulacaoService(SimulacaoRepository simulacaoRepository, SimulacaoCalculoService simulacaoCalculoService,
                            SimulacaoMapper simulacaoMapper, ClienteAuthService clienteAuthService,
                            ProdutoRecomendacaoService produtoRecomendacaoService, SimulacaoSolicitadaMapper simulacaoSolicitadaMapper,
                            PerfilRiscoRecalculoService perfilRiscoRecalculoService) {

        this.simulacaoRepository = simulacaoRepository;
        this.simulacaoCalculoService = simulacaoCalculoService;
        this.simulacaoMapper = simulacaoMapper;
        this.clienteAuthService = clienteAuthService;
        this.produtoRecomendacaoService = produtoRecomendacaoService;
        this.simulacaoSolicitadaMapper = simulacaoSolicitadaMapper;
        this.perfilRiscoRecalculoService = perfilRiscoRecalculoService;
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
    public SimulacaoSolicitadaResponse simularInvestimento(SimulacaoRequest request) {

        LOG.infof("Simulando investimento com parâmetros: valor=%f, prazo=%d meses e tipoProduto=%s",
                request.valor(), request.prazoMeses(), request.tipoProduto());

        Cliente cliente = clienteAuthService.getClienteAutenticado();
        LOG.infof("Cliente autenticado encontrado: ID %d, Nome: %s", cliente.getId(), cliente.getNome());

        Produto produto = produtoRecomendacaoService.recomendarProduto(cliente, request.tipoProduto());
        if (produto == null) {
            throw new NenhumProdutoValidadoParaSimulacaoException(cliente.getPerfilRisco(), cliente.getPontuacaoRisco(), request.tipoProduto());
        }

        Simulacao simulacao = simulacaoCalculoService.calcularSimulacao(cliente, produto, request.valor(), request.prazoMeses());
        simulacaoRepository.persist(simulacao);
        LOG.infof("Simulação persistida com ID: %d", simulacao.getId());

        List<Simulacao> simulacoesCliente = simulacaoRepository.list("cliente.id", cliente.getId());
        //TODO: AJUSTAR ESTE TRECHO QUANDO CRIAR FLUXO DE INVESTIMENTOS
        //List<Investimento> investimentosCliente = investimentoRepository.list("cliente.id", cliente.getId());

        LOG.infof("Perfil de risco antes do recalculo do cliente ID %d -> perfil=%s, pontuacao=%s",
                cliente.getId(),
                cliente.getPerfilRisco(),
                cliente.getPontuacaoRisco()
        );

        PerfilRiscoRecalculoService.ResultadoRecalculoPerfil resultadoPerfil =
                perfilRiscoRecalculoService.recalcularPerfil(cliente, simulacoesCliente, new ArrayList<>());

        cliente.setPontuacaoRisco(resultadoPerfil.novaPontuacaoRisco());
        cliente.setPerfilRisco(resultadoPerfil.novoPerfilRisco());

        LOG.infof(
                "Perfil recalculado do cliente ID %d -> perfil=%s, pontuacao=%s",
                cliente.getId(),
                cliente.getPerfilRisco(),
                cliente.getPontuacaoRisco()
        );

        return simulacaoSolicitadaMapper.toResponse(produto, simulacao);
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
