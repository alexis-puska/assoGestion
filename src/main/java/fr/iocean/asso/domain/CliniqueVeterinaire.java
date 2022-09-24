package fr.iocean.asso.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CliniqueVeterinaire.
 */
@Entity
@Table(name = "clinique_veterinaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CliniqueVeterinaire implements Serializable {

    private static final long serialVersionUID = 1662316772394343744L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "actif")
    private Boolean actif;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Adresse adresse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public CliniqueVeterinaire id(Long id) {
        this.setId(id);
        return this;
    }

    public CliniqueVeterinaire nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public CliniqueVeterinaire actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public CliniqueVeterinaire adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
