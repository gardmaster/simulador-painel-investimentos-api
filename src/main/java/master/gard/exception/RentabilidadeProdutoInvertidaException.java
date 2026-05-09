package master.gard.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RentabilidadeProdutoInvertidaException extends RuntimeException {

    private final BigDecimal rentabilidadeMin;
    private final BigDecimal rentabilidadeMax;

    public RentabilidadeProdutoInvertidaException(BigDecimal rentabilidadeMin, BigDecimal rentabilidadeMax) {
        this.rentabilidadeMin = rentabilidadeMin;
        this.rentabilidadeMax = rentabilidadeMax;
    }
}
