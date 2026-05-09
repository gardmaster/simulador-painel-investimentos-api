package master.gard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import master.gard.model.enums.ProdutoRisco;
import master.gard.model.enums.TipoProduto;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_produto", nullable = false)
    private TipoProduto tipoProduto;

    @Enumerated(EnumType.STRING)
    @Column(name = "produto_risco", nullable = false)
    private ProdutoRisco produtoRisco;

    @Column(name = "rentabilidade_mensal", nullable = false)
    private BigDecimal rentabilidadeMensal;

    @Column(name = "data_criacao", nullable = false)
    private Instant dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private Instant dataAtualizacao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = Instant.now();
        this.dataAtualizacao = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = Instant.now();
    }

}
