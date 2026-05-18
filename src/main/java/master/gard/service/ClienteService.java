package master.gard.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import master.gard.config.Messages;
import master.gard.dto.request.cliente.ClienteFiltroRequest;
import master.gard.dto.request.cliente.ClienteRequest;
import master.gard.dto.response.PageInfoResponse;
import master.gard.dto.response.cliente.ClientePageResponse;
import master.gard.dto.response.cliente.ClienteResponse;
import master.gard.dto.response.cliente.PerfilRiscoResponse;
import master.gard.exception.ClienteAutenticadoJaCadastradoException;
import master.gard.exception.ClienteNaoEncontradoException;
import master.gard.exception.DocumentoExistenteException;
import master.gard.exception.EmailExistenteException;
import master.gard.mapper.cliente.ClienteMapper;
import master.gard.mapper.cliente.PerfilRiscoMapper;
import master.gard.model.Cliente;
import master.gard.repository.ClienteRepository;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClienteService {

    private static final Logger LOG = Logger.getLogger(ClienteService.class);
    private final Messages msg;

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteAuthService clienteAuthService;
    private final PerfilRiscoMapper perfilRiscoMapper;

    @Inject
    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper,
                          ClienteAuthService clienteAuthService, Messages msg,
                          PerfilRiscoMapper perfilRiscoMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.clienteAuthService = clienteAuthService;
        this.msg = msg;
        this.perfilRiscoMapper = perfilRiscoMapper;
    }

    @Transactional
    public ClientePageResponse listarClientes(ClienteFiltroRequest filtro) {
        LOG.info("Listando todos os clientes");

        PanacheQuery<Cliente> query = clienteRepository.buscarFiltrado(filtro);
        query.page(Page.of(filtro.getPage() - 1, filtro.getPageSize()));

        List<ClienteResponse> clientes = clienteMapper.toResponseList(query.list());

        return new ClientePageResponse(
                clientes,
                new PageInfoResponse(filtro.getPage(), filtro.getPageSize(), query.count(), query.pageCount())
        );
    }

    @Transactional
    public ClienteResponse recuperarCliente(Long id) {
        LOG.infof("Recuperando cliente com ID: %d", id);

        return clienteRepository.findByIdOptional(id)
                .map(clienteMapper::toResponse)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    @Transactional
    public ClienteResponse cadastrarCliente(ClienteRequest request) {
        LOG.infof("Cadastrando novo cliente: %s", request.nome());

        LOG.info("Recuperando claim 'sub' do token JWT para associar ao cliente");
        String authUserId = clienteAuthService.getAuthUserId();
        validarClienteAutenticadoJaCadastrado(authUserId);

        validarDocumentoCadastrado(request.documento());
        validarEmailCadastrado(request.email());
        LOG.infof("Documento e email validados para cliente: %s", request.nome());

        Cliente cliente = clienteMapper.toEntity(request);
        cliente.setAuthUserId(authUserId);

        aplicarPontuacaoPadraoPorPerfil(cliente);

        clienteRepository.persist(cliente);
        LOG.infof("Cliente persistido com ID: %d", cliente.getId());

        return clienteMapper.toResponse(cliente);
    }

    @Transactional
    public ClienteResponse atualizarCliente(Long id, ClienteRequest request) {
        LOG.infof("Atualizando cliente com ID: %d", id);

        Cliente clienteExistente = validarClienteExistente(id);
        validarDocumentoCadastradoParaOutroCliente(request.documento(), id);
        validarEmailCadastradoParaOutroCliente(request.email(), id);
        LOG.infof("Documento e email validados para atualização do cliente ID: %d", id);

        clienteMapper.updateEntityFromRequest(request, clienteExistente);

        aplicarPontuacaoPadraoPorPerfil(clienteExistente);

        clienteRepository.persist(clienteExistente);
        LOG.infof("Cliente atualizado com ID: %d", id);

        return clienteMapper.toResponse(clienteExistente);
    }

    @Transactional
    public ClienteResponse obterClienteAutenticado() {
        LOG.info("Chamando ClienteService para buscar cliente autenticado");

        Cliente cliente = clienteAuthService.getClienteAutenticado();
        LOG.infof("Cliente autenticado encontrado: ID %d, Nome: %s", cliente.getId(), cliente.getNome());

        return clienteMapper.toResponse(cliente);
    }

    @Transactional
    public ClienteResponse atualizarCadastroClienteAutenticado(ClienteRequest request) {
        LOG.infof("Atualizando cadastro do cliente autenticado: %s", request.nome());

        Cliente clienteExistente = clienteAuthService.getClienteAutenticado();
        LOG.infof("Cliente autenticado encontrado para atualização: ID %d, Nome: %s", clienteExistente.getId(), clienteExistente.getNome());

        validarDocumentoCadastradoParaOutroCliente(request.documento(), clienteExistente.getId());
        validarEmailCadastradoParaOutroCliente(request.email(), clienteExistente.getId());
        LOG.infof("Documento e email validados para atualização do cliente autenticado ID: %d", clienteExistente.getId());

        clienteMapper.updateEntityFromRequest(request, clienteExistente);

        aplicarPontuacaoPadraoPorPerfil(clienteExistente);

        clienteRepository.persist(clienteExistente);
        LOG.infof("Cadastro do cliente autenticado atualizado com ID: %d", clienteExistente.getId());

        return clienteMapper.toResponse(clienteExistente);
    }

    public PerfilRiscoResponse getPerfilRiscoPorId(Long id) {
        LOG.infof("Obtendo perfil de risco para cliente ID: %d", id);

        Cliente cliente = validarClienteExistente(id);
        LOG.infof("Cliente encontrado para perfil de risco: ID %d, Nome: %s", cliente.getId(), cliente.getNome());

        if (cliente.getPerfilRisco() == null) {
            LOG.warnf("Cliente ID %d não possui um perfil de risco definido", id);
            return new PerfilRiscoResponse(cliente.getId(), null, null, "Perfil de risco não definido");
        }

        return perfilRiscoMapper.toResponse(cliente, msg);
    }





    private void validarDocumentoCadastrado(String documento) {
        LOG.infof("Validando se documento '%s' já está cadastrado para outro cliente", documento);
        if (clienteRepository.isDocumentoExistente(documento)) {
            throw new DocumentoExistenteException(documento);
        }
    }

    private void validarDocumentoCadastradoParaOutroCliente(String documento, Long id) {
        LOG.infof("Validando se documento '%s' já está cadastrado para outro cliente que não o ID: %d", documento, id);
        if (clienteRepository.isDocumentoCadastradoParaOutroCliente(documento, id)) {
            throw new DocumentoExistenteException(documento);
        }
    }

    private void validarEmailCadastrado(String email) {
        LOG.infof("Validando se email '%s' já está cadastrado para outro cliente", email);
        if (clienteRepository.isEmailExistente(email)) {
            throw new EmailExistenteException(email);
        }
    }

    private void validarEmailCadastradoParaOutroCliente(String email, Long id) {
        LOG.infof("Validando se email '%s' já está cadastrado para outro cliente que não o ID: %d", email, id);
        if (clienteRepository.isEmailCadastradoParaOutroCliente(email, id)) {
            throw new EmailExistenteException(email);
        }
    }

    private Cliente validarClienteExistente(Long id) {
        return clienteRepository.findByIdOptional(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    private void validarClienteAutenticadoJaCadastrado(String authUserId) {
        LOG.infof("Verificando se já existe um cliente cadastrado para authUserId: %s", authUserId);
        Optional<Cliente> clienteOpt = clienteRepository.findByAuthUserIdOptional(authUserId);

        if (clienteOpt.isPresent()) {
            throw new ClienteAutenticadoJaCadastradoException(
                    clienteAuthService.getPreferredUsername(),
                    authUserId,
                    clienteAuthService.getName(),
                    clienteAuthService.getEmail()
            );
        }
    }

    private void aplicarPontuacaoPadraoPorPerfil(Cliente cliente) {
        if (cliente.getPerfilRisco() == null) {
            return;
        }
        cliente.setPontuacaoRisco(cliente.getPerfilRisco().getPontuacaoPadrao());
    }
}
