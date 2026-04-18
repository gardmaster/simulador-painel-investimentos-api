package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.ClienteRequest;
import master.gard.dto.response.ClienteResponse;
import master.gard.exception.ClienteNaoEncontradoException;
import master.gard.model.Cliente;
import master.gard.repository.ClienteRepository;

import java.util.List;

@ApplicationScoped
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public List<ClienteResponse> listarClientes() {
        return clienteRepository.listAll()
                .stream()
                .map(ClienteResponse::fromEntity)
                .toList();
    }

    @Transactional
    public ClienteResponse recuperarCliente(Long id) {
        return clienteRepository.findByIdOptional(id).map(ClienteResponse::fromEntity)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado com ID: " + id));
    }

    @Transactional
    public Response cadastrarCliente(ClienteRequest request) {
        validarDocumentoNaoCadastrado(request.documento());
        validarEmailNaoCadastrado(request.email());

        Cliente cliente = ClienteRequest.toEntity(request);
        clienteRepository.persist(cliente);
        return Response.status(Response.Status.CREATED).entity(ClienteResponse.fromEntity(cliente)).build();
    }

    @Transactional
    public Response atualizarCliente(Long id, ClienteRequest request) {
        Cliente clienteExistente = validarClienteExistente(id);
        validarDocumentoCadastradoParaOutroCliente(request.documento(), id);
        validarEmailCadastradoParaOutroCliente(request.email(), id);

        clienteExistente.setDocumento(request.documento());
        clienteExistente.setEmail(request.email());
        clienteExistente.setNome(request.nome());
        clienteExistente.setPerfilRisco(request.perfilRisco());

        clienteRepository.persist(clienteExistente);
        return Response.ok(ClienteResponse.fromEntity(clienteExistente)).build();
    }

    private void validarDocumentoNaoCadastrado(String documento) {
        if (clienteRepository.isDocumentoExistente(documento)) {
            throw new WebApplicationException("Documento já cadastrado", Response.Status.BAD_REQUEST);
        }
    }

    private void validarDocumentoCadastradoParaOutroCliente(String documento, Long id) {
        if (clienteRepository.isDocumentoCadastradoParaOutroCliente(documento, id)) {
            throw new WebApplicationException("Documento já cadastrado para outro cliente", Response.Status.BAD_REQUEST);
        }
    }

    private void validarEmailNaoCadastrado(String email) {
        if (clienteRepository.isEmailExistente(email)) {
            throw new WebApplicationException("Email já cadastrado", Response.Status.BAD_REQUEST);
        }
    }

    private void validarEmailCadastradoParaOutroCliente(String email, Long id) {
        if (clienteRepository.isEmailCadastradoParaOutroCliente(email, id)) {
            throw new WebApplicationException("Email já cadastrado para outro cliente", Response.Status.BAD_REQUEST);
        }
    }

    private Cliente validarClienteExistente(Long id) {
        return clienteRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Cliente não encontrado com ID: " + id, Response.Status.NOT_FOUND));
    }
}
