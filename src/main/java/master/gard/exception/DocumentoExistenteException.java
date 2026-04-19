package master.gard.exception;

public class DocumentoExistenteException extends RuntimeException {

    public DocumentoExistenteException() {
        super();
    }

    public DocumentoExistenteException(String message) {
        super(message);
    }

}
