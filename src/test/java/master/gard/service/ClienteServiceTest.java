package master.gard.service;

import master.gard.dto.request.ClienteRequest;
import master.gard.dto.response.ClienteResponse;
import master.gard.exception.*;
import master.gard.model.Cliente;
import master.gard.model.enums.PerfilRisco;
import master.gard.repository.ClienteRepository;
import master.gard.util.DocumentoUtil;
import master.gard.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    private static final Long ID_CLIENTE_1 = 1L;
    private static final Long ID_CLIENTE_2 = 2L;
    private static final Long ID_CLIENTE_INEXISTENTE = 999L;

    private static final String AUTH_USER_ID_VALIDO = "auth-123";
    private static final String AUTH_USER_ID_VALIDO_SEM_CADASTRO = "auth-999";
    private static final String NOME_CLIENTE_1 = "Cliente 1";
    private static final String NOME_CLIENTE_2 = "Cliente 2";
    private static final String NOME_CLIENTE_NOVO = "Cliente Novo";

    private static final String DOCUMENTO_CLIENTE_1 = "12345678901";
    private static final String DOCUMENTO_CLIENTE_2 = "12345678902";
    private static final String EMAIL_CLIENTE_1 = "cliente1@test.com";
    private static final String EMAIL_CLIENTE_2 = "cliente2@test.com";
    private static final String EMAIL_CLIENTE_NOVO = "novo@test.com";

    private static final PerfilRisco PERFIL_CLIENTE_1 = PerfilRisco.CONSERVADOR;
    private static final PerfilRisco PERFIL_CLIENTE_2 = PerfilRisco.MODERADO;
    private static final PerfilRisco PERFIL_CLIENTE_NOVO = PerfilRisco.MODERADO;

    @Mock
    private ClienteRepository clienteRepositoryMock;

    @Mock
    private JwtUtil jwtUtilMock;

    @InjectMocks
    private ClienteService clienteServiceInjectedMock;

    @Test
    @DisplayName("Deve retornar lista de clientes response quando existirem clientes cadastrados")
    void deveRetornarListaClientes_quandoExistiremClientesCadastrados() {
        Cliente cliente1 = montarCliente(ID_CLIENTE_1, NOME_CLIENTE_1, DOCUMENTO_CLIENTE_1, EMAIL_CLIENTE_1, PERFIL_CLIENTE_1);
        Cliente cliente2 = montarCliente(ID_CLIENTE_2, NOME_CLIENTE_2, DOCUMENTO_CLIENTE_2, EMAIL_CLIENTE_2, PERFIL_CLIENTE_2);
        when(clienteRepositoryMock.listAll()).thenReturn(List.of(cliente1, cliente2));

        List<ClienteResponse> resposta = clienteServiceInjectedMock.listarClientes();

        assertNotNull(resposta);
        assertEquals(2, resposta.size());
        assertEquals(NOME_CLIENTE_1, resposta.getFirst().nome());
        assertEquals(NOME_CLIENTE_2, resposta.get(1).nome());
        verify(clienteRepositoryMock).listAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem clientes cadastrados")
    void deveRetornarListaVazia_quandoNaoExistiremClientesCadastrados() {
        when(clienteRepositoryMock.listAll()).thenReturn(List.of());

        List<ClienteResponse> resposta = clienteServiceInjectedMock.listarClientes();

        assertNotNull(resposta);
        assertTrue(resposta.isEmpty());
        verify(clienteRepositoryMock).listAll();
    }

    @Test
    @DisplayName("Deve retornar cliente response quando cliente for encontrado por ID")
    void deveRetornarClienteResponse_quandoClienteForEncontradoPorId() {
        Cliente cliente = montarCliente(ID_CLIENTE_1, NOME_CLIENTE_1, DOCUMENTO_CLIENTE_1, EMAIL_CLIENTE_1, PERFIL_CLIENTE_1);
        when(clienteRepositoryMock.findByIdOptional(ID_CLIENTE_1)).thenReturn(Optional.of(cliente));

        ClienteResponse resposta = clienteServiceInjectedMock.recuperarCliente(ID_CLIENTE_1);

        assertNotNull(resposta);
        assertEquals(ID_CLIENTE_1, resposta.id());
        verify(clienteRepositoryMock).findByIdOptional(ID_CLIENTE_1);
    }

    @Test
    @DisplayName("Deve lançar ClienteNaoEncontradoException quando cliente não for encontrado por ID")
    void deveLancarClienteNaoEncontradoException_quandoClienteNaoForEncontradoPorId() {
        when(clienteRepositoryMock.findByIdOptional(ID_CLIENTE_1)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteServiceInjectedMock.recuperarCliente(ID_CLIENTE_1));

        verify(clienteRepositoryMock).findByIdOptional(ID_CLIENTE_1);
    }

    @Test
    @DisplayName("Deve cadastrar cliente quando request for valida e dados nao existirem")
    void deveCadastrarCliente_quandoDadosForemValidos() {
        ClienteRequest request = montarRequestPadrao();
        mockarPreCondicoesCadastroValido(request, AUTH_USER_ID_VALIDO);

        ClienteResponse resposta = clienteServiceInjectedMock.cadastrarCliente(request);

        assertNotNull(resposta);
        assertEquals(NOME_CLIENTE_NOVO, resposta.nome());
        assertEquals(DocumentoUtil.formatarCpfCnpj(DOCUMENTO_CLIENTE_1), resposta.documento());
        assertEquals(EMAIL_CLIENTE_NOVO, resposta.email());
        assertEquals(PERFIL_CLIENTE_NOVO.name(), resposta.perfilRisco());

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepositoryMock).persist(captor.capture());

        Cliente persistido = captor.getValue();
        assertEquals(AUTH_USER_ID_VALIDO, persistido.getAuthUserId());
        assertEquals(NOME_CLIENTE_NOVO, persistido.getNome());
        assertEquals(DOCUMENTO_CLIENTE_1, persistido.getDocumento());
        assertEquals(EMAIL_CLIENTE_NOVO, persistido.getEmail());
        assertEquals(PERFIL_CLIENTE_NOVO, persistido.getPerfilRisco());
    }

    @Test
    @DisplayName("Deve lancar ClienteAutenticadoJaCadastradoException quando authUserId ja possuir cadastro")
    void deveLancarClienteAutenticadoJaCadastradoException_quandoAuthUserJaExistir() {
        ClienteRequest request = montarRequestPadrao();
        Cliente existente = new Cliente();
        existente.setNome("Cliente Existente");
        existente.setEmail("existente@test.com");

        when(jwtUtilMock.getSubject()).thenReturn(AUTH_USER_ID_VALIDO);
        when(clienteRepositoryMock.findByAuthUserIdOptional(AUTH_USER_ID_VALIDO)).thenReturn(Optional.of(existente));

        assertThrows(ClienteAutenticadoJaCadastradoException.class,
                () -> clienteServiceInjectedMock.cadastrarCliente(request));

        verify(clienteRepositoryMock, never()).persist(any(Cliente.class));
        verify(clienteRepositoryMock, never()).isDocumentoExistente(any());
        verify(clienteRepositoryMock, never()).isEmailExistente(any());
    }

    @Test
    @DisplayName("Deve lancar DocumentoExistenteException quando documento ja estiver cadastrado")
    void deveLancarDocumentoExistenteException_quandoDocumentoJaExistir() {
        ClienteRequest request = montarRequestPadrao();

        when(jwtUtilMock.getSubject()).thenReturn(AUTH_USER_ID_VALIDO);
        when(clienteRepositoryMock.findByAuthUserIdOptional(AUTH_USER_ID_VALIDO)).thenReturn(Optional.empty());
        when(clienteRepositoryMock.isDocumentoExistente(DOCUMENTO_CLIENTE_1)).thenReturn(true);

        assertThrows(DocumentoExistenteException.class,
                () -> clienteServiceInjectedMock.cadastrarCliente(request));

        verify(clienteRepositoryMock, never()).persist(any(Cliente.class));
        verify(clienteRepositoryMock, never()).isEmailExistente(any());
    }

    @Test
    @DisplayName("Deve lancar EmailExistenteException quando email ja estiver cadastrado")
    void deveLancarEmailExistenteException_quandoEmailJaExistir() {
        // Arrange
        ClienteRequest request = montarRequestPadrao();

        when(jwtUtilMock.getSubject()).thenReturn(AUTH_USER_ID_VALIDO);
        when(clienteRepositoryMock.findByAuthUserIdOptional(AUTH_USER_ID_VALIDO)).thenReturn(Optional.empty());
        when(clienteRepositoryMock.isDocumentoExistente(DOCUMENTO_CLIENTE_1)).thenReturn(false);
        when(clienteRepositoryMock.isEmailExistente(EMAIL_CLIENTE_NOVO)).thenReturn(true);

        // Act + Assert
        assertThrows(EmailExistenteException.class,
                () -> clienteServiceInjectedMock.cadastrarCliente(request));

        verify(clienteRepositoryMock, never()).persist(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve atualizar cadastro do cliente recuperado por ID")
    void deveAtualizarCadastroCliente_quandoIdExistenteComDadosValidos() {
        ClienteRequest request = montarRequestPadrao();
        Cliente clienteExistente = montarCliente(ID_CLIENTE_1, NOME_CLIENTE_1, DOCUMENTO_CLIENTE_1, EMAIL_CLIENTE_1, PERFIL_CLIENTE_1);

        when(clienteRepositoryMock.findByIdOptional(ID_CLIENTE_1)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepositoryMock.isDocumentoCadastradoParaOutroCliente(DOCUMENTO_CLIENTE_1, ID_CLIENTE_1)).thenReturn(false);
        when(clienteRepositoryMock.isEmailCadastradoParaOutroCliente(EMAIL_CLIENTE_NOVO, ID_CLIENTE_1)).thenReturn(false);

        ClienteResponse resposta = clienteServiceInjectedMock.atualizarCliente(ID_CLIENTE_1, request);

        assertNotNull(resposta);
        assertEquals(NOME_CLIENTE_NOVO, resposta.nome());
        assertEquals(DocumentoUtil.formatarCpfCnpj(DOCUMENTO_CLIENTE_1), resposta.documento());
        assertEquals(EMAIL_CLIENTE_NOVO, resposta.email());
        assertEquals(PERFIL_CLIENTE_NOVO.name(), resposta.perfilRisco());
    }

    @Test
    @DisplayName("Deve lancar ClienteNaoEncontradoException ao tentar atualizar cadastro de cliente inexistente")
    void deveLancarClienteNaoEncontradoException_quandoAtualizarCadastroClienteInexistente() {
        ClienteRequest request = montarRequestPadrao();

        when(clienteRepositoryMock.findByIdOptional(ID_CLIENTE_INEXISTENTE)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteServiceInjectedMock.atualizarCliente(ID_CLIENTE_INEXISTENTE, request));

        verify(clienteRepositoryMock, never()).persist(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lancar DocumentoExistenteException ao tentar atualizar cadastro com documento já existente para outro cliente")
    void deveLancarDocumentoExistenteException_quandoAtualizarCadastroComDocumentoExistente() {
        ClienteRequest request = montarRequestPadrao();
        Cliente clienteExistente = montarCliente(ID_CLIENTE_1, NOME_CLIENTE_1, DOCUMENTO_CLIENTE_1, EMAIL_CLIENTE_1, PERFIL_CLIENTE_1);

        when(clienteRepositoryMock.findByIdOptional(ID_CLIENTE_1)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepositoryMock.isDocumentoCadastradoParaOutroCliente(DOCUMENTO_CLIENTE_1, ID_CLIENTE_1)).thenReturn(true);

        assertThrows(DocumentoExistenteException.class,
                () -> clienteServiceInjectedMock.atualizarCliente(ID_CLIENTE_1, request));

        verify(clienteRepositoryMock, never()).persist(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lancar EmailExistenteException ao tentar atualizar cadastro com email já existente para outro cliente")
    void deveLancarEmailExistenteException_quandoAtualizarCadastroComEmailExistente(){
        ClienteRequest request = montarRequestPadrao();
        Cliente clienteExistente = montarCliente(ID_CLIENTE_1, NOME_CLIENTE_1, DOCUMENTO_CLIENTE_1, EMAIL_CLIENTE_1, PERFIL_CLIENTE_1);

        when(clienteRepositoryMock.findByIdOptional(ID_CLIENTE_1)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepositoryMock.isDocumentoCadastradoParaOutroCliente(DOCUMENTO_CLIENTE_1, ID_CLIENTE_1)).thenReturn(false);
        when(clienteRepositoryMock.isEmailCadastradoParaOutroCliente(EMAIL_CLIENTE_NOVO, ID_CLIENTE_1)).thenReturn(true);

        assertThrows(EmailExistenteException.class,
                () -> clienteServiceInjectedMock.atualizarCliente(ID_CLIENTE_1, request));

        verify(clienteRepositoryMock, never()).persist(any(Cliente.class));
    }

    @Test
    @DisplayName("Recuperar informações do cliente autenticado")
    void deveRecuperarInformacoesClienteAutenticado_quandoClienteExistir() {
        Cliente clienteExistente = montarCliente(ID_CLIENTE_1, NOME_CLIENTE_1, DOCUMENTO_CLIENTE_1, EMAIL_CLIENTE_1, PERFIL_CLIENTE_1);

        when(jwtUtilMock.getSubject()).thenReturn(AUTH_USER_ID_VALIDO);
        when(clienteRepositoryMock.findByAuthUserIdOptional(AUTH_USER_ID_VALIDO)).thenReturn(Optional.of(clienteExistente));

        ClienteResponse resposta = clienteServiceInjectedMock.obterClienteAutenticado();

        assertNotNull(resposta);
        assertEquals(NOME_CLIENTE_1, resposta.nome());
        assertEquals(DocumentoUtil.formatarCpfCnpj(DOCUMENTO_CLIENTE_1), resposta.documento());
        assertEquals(EMAIL_CLIENTE_1, resposta.email());
        assertEquals(PERFIL_CLIENTE_1.name(), resposta.perfilRisco());

        verify(clienteRepositoryMock).findByAuthUserIdOptional(AUTH_USER_ID_VALIDO);
    }

    @Test
    @DisplayName("Deve lançar ClienteAutenticadoNaoEncontradoException ao tentar recuperar informações do " +
            "cliente autenticado sem cadastro na aplicação")
    void deveLancarClienteAutenticadoNaoEncontradoException_quandoRecuperarClienteAutenticadoSemCadastro() {
        when(jwtUtilMock.getSubject()).thenReturn(AUTH_USER_ID_VALIDO_SEM_CADASTRO);
        when(clienteRepositoryMock.findByAuthUserIdOptional(AUTH_USER_ID_VALIDO_SEM_CADASTRO)).thenReturn(Optional.empty());

        assertThrows(ClienteAutenticadoSemCadastroException.class,
                () -> clienteServiceInjectedMock.obterClienteAutenticado());

        verify(clienteRepositoryMock).findByAuthUserIdOptional(AUTH_USER_ID_VALIDO_SEM_CADASTRO);
    }

    @Test
    @DisplayName("Deve atualizar informações do cliente autenticado")
    void deveAtualizarInformacoesClienteAutenticado_quandoDadosForemValidos() {
        ClienteRequest request = montarRequestPadrao();
        Cliente clienteExistente = montarCliente(ID_CLIENTE_1, NOME_CLIENTE_1, DOCUMENTO_CLIENTE_1, EMAIL_CLIENTE_1, PERFIL_CLIENTE_1);

        when(jwtUtilMock.getSubject()).thenReturn(AUTH_USER_ID_VALIDO);
        when(clienteRepositoryMock.findByAuthUserIdOptional(AUTH_USER_ID_VALIDO)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepositoryMock.isDocumentoCadastradoParaOutroCliente(DOCUMENTO_CLIENTE_1, ID_CLIENTE_1)).thenReturn(false);
        when(clienteRepositoryMock.isEmailCadastradoParaOutroCliente(EMAIL_CLIENTE_NOVO, ID_CLIENTE_1)).thenReturn(false);

        ClienteResponse resposta = clienteServiceInjectedMock.atualizarCadastroClienteAutenticado(request);

        assertNotNull(resposta);
        assertEquals(NOME_CLIENTE_NOVO, resposta.nome());
        assertEquals(DocumentoUtil.formatarCpfCnpj(DOCUMENTO_CLIENTE_1), resposta.documento());
        assertEquals(EMAIL_CLIENTE_NOVO, resposta.email());
        assertEquals(PERFIL_CLIENTE_NOVO.name(), resposta.perfilRisco());

        verify(clienteRepositoryMock).persist(clienteExistente);
    }

    private void mockarPreCondicoesCadastroValido(ClienteRequest request, String authUserId) {
        when(jwtUtilMock.getSubject()).thenReturn(authUserId);
        when(clienteRepositoryMock.findByAuthUserIdOptional(authUserId)).thenReturn(Optional.empty());
        when(clienteRepositoryMock.isDocumentoExistente(request.documento())).thenReturn(false);
        when(clienteRepositoryMock.isEmailExistente(request.email())).thenReturn(false);
    }

    private ClienteRequest montarRequestPadrao() {
        return new ClienteRequest(
                NOME_CLIENTE_NOVO,
                DOCUMENTO_CLIENTE_1,
                EMAIL_CLIENTE_NOVO,
                PERFIL_CLIENTE_NOVO
        );
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