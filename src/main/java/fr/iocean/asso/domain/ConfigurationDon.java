package fr.iocean.asso.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "objet", length = 128)
    private String objet;

    @Column(name = "objet1", length = 128)
    private String objet1;

    @Column(name = "objet2", length = 128)
    private String objet2;

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

    public ConfigurationDon objet1(String objet1) {
        this.setObjet1(objet1);
        return this;
    }

    public ConfigurationDon objet2(String objet2) {
        this.setObjet2(objet2);
        return this;
    }

    public ConfigurationDon adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
