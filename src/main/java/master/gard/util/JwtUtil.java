package master.gard.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import master.gard.exception.ClaimSubInexistenteException;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;

@ApplicationScoped
public class JwtUtil {

    private final JsonWebToken jwt;

    @Inject
    public JwtUtil(JsonWebToken jwt) {
        this.jwt = jwt;
    }

    public String getSubject() {
        String sub = jwt.getSubject();
        if (sub == null || sub.isBlank()) {
            throw new ClaimSubInexistenteException(getName().orElse("N/A"), getEmail().orElse("N/A"), getPreferredUsername().orElse("N/A"));
        }
        return sub;
    }

    public Optional<String> getPreferredUsername() {
        return Optional.ofNullable(jwt.getClaim("preferred_username"));
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(jwt.getClaim("email"));
    }

    public Optional<String> getName() {
        return Optional.ofNullable(jwt.getClaim("name"));
    }
}
