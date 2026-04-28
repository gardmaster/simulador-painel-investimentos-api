package master.gard.exception;

import lombok.Getter;

@Getter
public class ProdutoNaoEncontradoException extends RuntimeException {

    private final Long id;

    public ProdutoNaoEncontradoException(Long id) {
        this.id = id;
    }

}
