package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.exception.ClienteAutenticadoSemCadastroException;
import master.gard.model.Cliente;
import master.gard.repository.ClienteRepository;
import master.gard.util.JwtUtil;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ClienteAuthService {

    private static final Logger LOG = Logger.getLogger(ClienteAuthService.class);

    private final ClienteRepository clienteRepository;
    private final JwtUtil jwtUtil;

    public ClienteAuthService(ClienteRepository clienteRepository, JwtUtil jwtUtil) {
        this.clienteRepository = clienteRepository;
        this.jwtUtil = jwtUtil;
    }

    public String getAuthUserId() {
        String authUserId = jwtUtil.getSubject();
        LOG.infof("AuthUserId extraido do token: %s", authUserId);
        return authUserId;
    }

    public Cliente getClienteAutenticado() {
        String authUserId = getAuthUserId();
        return getClientePorAuthUserId(authUserId);
    }

    public Cliente getClientePorAuthUserId(String authUserId) {
        return clienteRepository.findByAuthUserIdOptional(authUserId)
                .orElseThrow(() -> new ClienteAutenticadoSemCadastroException(
                        jwtUtil.getPreferredUsername().orElse("N/A"),
                        authUserId,
                        jwtUtil.getName().orElse("N/A"),
                        jwtUtil.getEmail().orElse("N/A")
                ));
    }

    public String getPreferredUsername() {
        return jwtUtil.getPreferredUsername().orElse("N/A");
    }

    public String getEmail() {
        return jwtUtil.getEmail().orElse("N/A");
    }

    public String getName() {
        return jwtUtil.getName().orElse("N/A");
    }
}
