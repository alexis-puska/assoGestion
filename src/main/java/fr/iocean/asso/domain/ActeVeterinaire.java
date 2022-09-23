package fr.iocean.asso.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ActeVeterinaire.
 */
@Entity
@Table(name = "acte_veterinaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ActeVeterinaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @ManyToOne
    private VisiteVeterinaire visiteVeterinaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ActeVeterinaire id(Long id) {
        this.setId(id);
        return this;
    }

    public ActeVeterinaire libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public ActeVeterinaire visiteVeterinaire(VisiteVeterinaire visiteVeterinaire) {
        this.setVisiteVeterinaire(visiteVeterinaire);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here
}
