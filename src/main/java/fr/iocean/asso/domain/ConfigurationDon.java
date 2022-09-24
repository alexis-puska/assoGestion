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
 * A ConfigurationDon.
 */
@Entity
@Table(name = "configuration_don")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ConfigurationDon implements Serializable {

    private static final long serialVersionUID = 5405134867621671569L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "objet")
    private String objet;

    @Column(name = "signataire")
    private String signataire;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Adresse adresse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ConfigurationDon id(Long id) {
        this.setId(id);
        return this;
    }

    public ConfigurationDon denomination(String denomination) {
        this.setDenomination(denomination);
        return this;
    }

    public ConfigurationDon objet(String objet) {
        this.setObjet(objet);
        return this;
    }

    public ConfigurationDon signataire(String signataire) {
        this.setSignataire(signataire);
        return this;
    }

    public void setSignataire(String signataire) {
        this.signataire = signataire;
    }

    public ConfigurationDon adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
