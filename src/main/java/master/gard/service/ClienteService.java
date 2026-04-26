package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.ClienteRequest;
import master.gard.dto.response.ClienteResponse;
import master.gard.exception.*;
import master.gard.model.Cliente;
import master.gard.repository.ClienteRepository;
import master.gard.util.JwtUtil;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClienteService {

    private static final Logger LOG = Logger.getLogger(ClienteService.class);
    private final ClienteRepository clienteRepository;
    private final JwtUtil jwtUtil;

    @Inject
    public ClienteService(ClienteRepository clienteRepository, JwtUtil jwtUtil) {
        this.clienteRepository = clienteRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public List<ClienteResponse> listarClientes() {
        LOG.info("Listando todos os clientes");

        return clienteRepository.listAll()
                .stream()
                .map(ClienteResponse::fromEntity)
                .toList();
    }

    @Transactional
    public ClienteResponse recuperarCliente(Long id) {
        LOG.infof("Recuperando cliente com ID: %d", id);

        return clienteRepository.findByIdOptional(id).map(ClienteResponse::fromEntity)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    @Transactional
    public ClienteResponse cadastrarCliente(ClienteRequest request) {
        LOG.infof("Cadastrando novo cliente: %s", request.nome());

        LOG.info("Recuperando claim 'sub' do token JWT para associar ao cliente");
        String authUserId = jwtUtil.getSubject();
        validarClienteAutenticadoJaCadastrado(authUserId);

        validarDocumentoCadastrado(request.documento());
        validarEmailCadastrado(request.email());
        LOG.infof("Documento e email validados para cliente: %s", request.nome());

        Cliente cliente = ClienteRequest.toEntity(request);
        cliente.setAuthUserId(authUserId);
        clienteRepository.persist(cliente);
        LOG.infof("Cliente persistido com ID: %d", cliente.getId());

        return ClienteResponse.fromEntity(cliente);
    }

    @Transactional
    public ClienteResponse atualizarCliente(Long id, ClienteRequest request) {
        LOG.infof("Atualizando cliente com ID: %d", id);

        Cliente clienteExistente = validarClienteExistente(id);
        validarDocumentoCadastradoParaOutroCliente(request.documento(), id);
        validarEmailCadastradoParaOutroCliente(request.email(), id);
        LOG.infof("Documento e email validados para atualização do cliente ID: %d", id);

        clienteExistente.setDocumento(request.documento());
        clienteExistente.setEmail(request.email());
        clienteExistente.setNome(request.nome());
        clienteExistente.setPerfilRisco(request.perfilRisco());
        clienteRepository.persist(clienteExistente);
        LOG.infof("Cliente atualizado com ID: %d", id);

        return ClienteResponse.fromEntity(clienteExistente);
    }

    @Transactional
    public ClienteResponse obterClienteAutenticado() {
        LOG.info("Chamando ClienteService para buscar cliente autenticado");

        String authUserId = jwtUtil.getSubject();
        LOG.infof("AuthUserId extraído do token: %s", authUserId);

        Cliente cliente = getClienteExistentePorUserAuthId(authUserId);

        LOG.infof("Cliente autenticado encontrado: ID %d, Nome: %s", cliente.getId(), cliente.getNome());

        return ClienteResponse.fromEntity(cliente);
    }

    @Transactional
    public ClienteResponse atualizarCadastroClienteAutenticado(ClienteRequest request) {
        LOG.infof("Atualizando cadastro do cliente autenticado: %s", request.nome());

        String authUserId = jwtUtil.getSubject();
        LOG.infof("AuthUserId extraído do token: %s", authUserId);

        Cliente clienteExistente = getClienteExistentePorUserAuthId(authUserId);

        validarDocumentoCadastradoParaOutroCliente(request.documento(), clienteExistente.getId());
        validarEmailCadastradoParaOutroCliente(request.email(), clienteExistente.getId());
        LOG.infof("Documento e email validados para atualização do cliente autenticado ID: %d", clienteExistente.getId());

        clienteExistente.setDocumento(request.documento());
        clienteExistente.setEmail(request.email());
        clienteExistente.setNome(request.nome());
        clienteExistente.setPerfilRisco(request.perfilRisco());
        clienteRepository.persist(clienteExistente);
        LOG.infof("Cadastro do cliente autenticado atualizado com ID: %d", clienteExistente.getId());

        return ClienteResponse.fromEntity(clienteExistente);
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

    private Cliente getClienteExistentePorUserAuthId(String authUserId) {
        return clienteRepository.findByAuthUserIdOptional(authUserId)
                .orElseThrow(() -> new ClienteAutenticadoSemCadastroException(
                        jwtUtil.getPreferredUsername().orElse("N/A"),
                        authUserId, jwtUtil.getName().orElse("N/A"),
                        jwtUtil.getEmail().orElse("N/A")));
    }

    private void validarClienteAutenticadoJaCadastrado(String authUserId) {
        LOG.infof("Verificando se já existe um cliente cadastrado para authUserId: %s", authUserId);
        Optional<Cliente> clienteOpt = clienteRepository.findByAuthUserIdOptional(authUserId);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            throw new ClienteAutenticadoJaCadastradoException(
                    jwtUtil.getPreferredUsername().orElse("N/A"),
                    authUserId,
                    cliente.getNome(),
                    cliente.getEmail()
            );
        }
    }
}
