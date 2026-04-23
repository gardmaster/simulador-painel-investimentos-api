package master.gard.exception;

import lombok.Getter;

@Getter
public class EmailExistenteException extends RuntimeException {

    private final String email;

    public EmailExistenteException(String email) {
        this.email = email;
    }

}
