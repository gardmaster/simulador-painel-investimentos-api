package master.gard.exception;

import lombok.Getter;

@Getter
public class DocumentoExistenteException extends RuntimeException {

    private final String documento;

    public DocumentoExistenteException(String documento) {
        this.documento = documento;
    }

}
