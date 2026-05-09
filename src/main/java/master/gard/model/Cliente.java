package master.gard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import master.gard.model.enums.PerfilRisco;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_user_id", nullable = false, unique = true)
    private String authUserId;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "documento", nullable = false, unique = true)
    private String documento;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_risco", nullable = false)
    private PerfilRisco perfilRisco;

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
