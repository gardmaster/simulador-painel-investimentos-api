package master.gard.exception;

public class EmailExistenteException extends RuntimeException {

    public EmailExistenteException() {
        super();
    }

    public EmailExistenteException(String message) {
        super(message);
    }

}
