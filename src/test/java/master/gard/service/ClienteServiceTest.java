package master.gard.service;

import master.gard.dto.response.ClienteResponse;
import master.gard.exception.ClienteNaoEncontradoException;
import master.gard.model.Cliente;
import master.gard.model.enums.PerfilRisco;
import master.gard.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepositoryMock;

    @InjectMocks
    private ClienteService clienteServiceInjectedMock;

    @Test
    @DisplayName("Deve retornar lista de clientes response quando existirem clientes cadastrados")
    void deveRetornarListaClientes_quandoExistiremClientesCadastrados() {
        Cliente cliente1 = montarCliente(1L, "Cliente 1", "12345678901",
                "cliente1@test.com", PerfilRisco.CONSERVADOR);

        Cliente cliente2 = montarCliente(2L, "Cliente 2", "12345678902",
                "cliente2@test.com", PerfilRisco.MODERADO);

        when(clienteRepositoryMock.listAll()).thenReturn(List.of(cliente1, cliente2));

        List<ClienteResponse> resposta = clienteServiceInjectedMock.listarClientes();

        assertNotNull(resposta);
        assertEquals(2, resposta.size());
        assertEquals("Cliente 1", resposta.getFirst().nome());
        assertEquals("Cliente 2", resposta.get(1).nome());
        verify(clienteRepositoryMock).listAll();

    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem clientes cadastrados")
    void deveRetornarListaVazia_quandoNaoExistiremClientesCadastrados() {
        when(clienteRepositoryMock.listAll()).thenReturn(List.of());

        List<ClienteResponse> resposta = clienteServiceInjectedMock.listarClientes();

        assertNotNull(resposta);
        assertEquals(0, resposta.size());
        verify(clienteRepositoryMock).listAll();
    }

    @Test
    @DisplayName("Deve retornar cliente response quando cliente for encontrado por ID")
    void deveRetornarClienteResponse_quandoClienteForEncontradoPorId() {
        Cliente cliente = montarCliente(1L, "Cliente 1", "12345678901",
                "cliente1@test.com", PerfilRisco.CONSERVADOR);

        when(clienteRepositoryMock.findByIdOptional(1L)).thenReturn(Optional.of(cliente));

        ClienteResponse resposta = clienteServiceInjectedMock.recuperarCliente(1L);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        verify(clienteRepositoryMock).findByIdOptional(1L);
    }

    @Test
    @DisplayName("Deve lançar ClienteNaoEncontradoException quando cliente não for encontrado por ID")
    void deveLancarClienteNaoEncontradoException_quandoClienteNaoForEncontradoPorId() {
        when(clienteRepositoryMock.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> clienteServiceInjectedMock.recuperarCliente(1L));

        verify(clienteRepositoryMock).findByIdOptional(1L);
    }

    private Cliente montarCliente(Long id, String nome, String documento, String email, PerfilRisco perfilRisco) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setDocumento(documento);
        cliente.setEmail(email);
        cliente.setPerfilRisco(perfilRisco);
        return cliente;
    }
}