package master.gard.exception;

import lombok.Getter;

@Getter
public class ClienteAutenticadoJaCadastradoException extends RuntimeException {

    private final String username;
    private final String authUserId;
    private final String nome;
    private final String email;

    public ClienteAutenticadoJaCadastradoException(String username, String authUserId, String nome, String email) {
        this.username = username;
        this.authUserId = authUserId;
        this.nome = nome;
        this.email = email;
    }

}
