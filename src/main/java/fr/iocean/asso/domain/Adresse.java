package fr.iocean.asso.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Adresse.
 */
@Entity
@Table(name = "adresse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Adresse implements Serializable {

    private static final long serialVersionUID = -3279117807804195129L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @NotNull
    @Column(name = "rue", nullable = false)
    private String rue;

    @NotNull
    @Column(name = "code_postale", nullable = false)
    private String codePostale;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Adresse id(Long id) {
        this.setId(id);
        return this;
    }

    public Adresse numero(Integer numero) {
        this.setNumero(numero);
        return this;
    }

    public Adresse rue(String rue) {
        this.setRue(rue);
        return this;
    }

    public Adresse codePostale(String codePostale) {
        this.setCodePostale(codePostale);
        return this;
    }

    public Adresse ville(String ville) {
        this.setVille(ville);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
