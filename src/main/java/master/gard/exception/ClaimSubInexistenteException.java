package master.gard.exception;

import lombok.Getter;

@Getter
public class ClaimSubInexistenteException extends RuntimeException {

    private final String preferredUsername;
    private final String nome;
    private final String email;

    public ClaimSubInexistenteException(String nome, String email, String preferredUsername) {
        this.nome = nome;
        this.email = email;
        this.preferredUsername = preferredUsername;
    }

}
