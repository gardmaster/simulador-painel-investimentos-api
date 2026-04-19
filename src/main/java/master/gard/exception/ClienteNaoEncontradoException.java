package master.gard.exception;

import lombok.Getter;

@Getter
public class ClienteNaoEncontradoException extends RuntimeException {

    private final Long clienteId;

    public ClienteNaoEncontradoException(Long clienteId) {
        this.clienteId = clienteId;
    }

}
