package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.ClienteRequest;
import master.gard.dto.response.ClienteResponse;
import master.gard.exception.ClienteAutenticadoSemCadastroException;
import master.gard.exception.ClienteNaoEncontradoException;
import master.gard.exception.DocumentoExistenteException;
import master.gard.exception.EmailExistenteException;
import master.gard.model.Cliente;
import master.gard.repository.ClienteRepository;
import master.gard.util.JwtUtil;
import org.jboss.logging.Logger;

import java.util.List;

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
    public Response cadastrarCliente(ClienteRequest request) {
        LOG.infof("Cadastrando novo cliente: %s", request.nome());

        validarDocumentoCadastrado(request.documento());
        validarEmailCadastrado(request.email());
        LOG.infof("Documento e email validados para cliente: %s", request.nome());


        Cliente cliente = ClienteRequest.toEntity(request);
        clienteRepository.persist(cliente);
        LOG.infof("Cliente persistido com ID: %d", cliente.getId());

        return Response.status(Response.Status.CREATED).entity(ClienteResponse.fromEntity(cliente)).build();
    }

    @Transactional
    public Response atualizarCliente(Long id, ClienteRequest request) {
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

        return Response.ok(ClienteResponse.fromEntity(clienteExistente)).build();
    }

    @Transactional
    public Response obterClienteAutenticado() {
        LOG.info("Chamando ClienteService para buscar cliente autenticado");

        String authUserId = jwtUtil.getSubject();
        LOG.infof("AuthUserId extraído do token: %s", authUserId);

        Cliente cliente = clienteRepository.findByAuthUserIdOptional(authUserId)
                .orElseThrow(() -> new ClienteAutenticadoSemCadastroException(
                        jwtUtil.getPreferredUsername().orElse("N/A"),
                        authUserId, jwtUtil.getName().orElse("N/A"),
                        jwtUtil.getEmail().orElse("N/A")));

        LOG.infof("Cliente autenticado encontrado: ID %d, Nome: %s", cliente.getId(), cliente.getNome());

        return Response.ok(ClienteResponse.fromEntity(cliente)).build();
    }








    private void validarDocumentoCadastrado(String documento) {
        if (clienteRepository.isDocumentoExistente(documento)) {
            throw new DocumentoExistenteException(documento);
        }
    }

    private void validarDocumentoCadastradoParaOutroCliente(String documento, Long id) {
        if (clienteRepository.isDocumentoCadastradoParaOutroCliente(documento, id)) {
            throw new DocumentoExistenteException(documento);
        }
    }

    private void validarEmailCadastrado(String email) {
        if (clienteRepository.isEmailExistente(email)) {
            throw new EmailExistenteException(email);
        }
    }

    private void validarEmailCadastradoParaOutroCliente(String email, Long id) {
        if (clienteRepository.isEmailCadastradoParaOutroCliente(email, id)) {
            throw new EmailExistenteException(email);
        }
    }

    private Cliente validarClienteExistente(Long id) {
        return clienteRepository.findByIdOptional(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

}
