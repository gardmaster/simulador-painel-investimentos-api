package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.dto.response.ClienteResponse;
import master.gard.repository.ClienteRepository;

import java.util.List;

@ApplicationScoped
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponse> listarClientes() {
        return clienteRepository.listAll()
                .stream()
                .map(ClienteResponse::fromEntity)
                .toList();
    }

    public ClienteResponse recuperarCliente(Long id) {
        return clienteRepository.findByIdOptional(id).map(ClienteResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
    }

}
