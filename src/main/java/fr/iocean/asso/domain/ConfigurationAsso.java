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
 * A ConfigurationAsso.
 */
@Entity
@Table(name = "configuration_asso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ConfigurationAsso implements Serializable {

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

    @Column(name = "siret", length = 17)
    private String siret;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone", length = 20)
    private String telephone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ConfigurationAsso id(Long id) {
        this.setId(id);
        return this;
    }

    public ConfigurationAsso denomination(String denomination) {
        this.setDenomination(denomination);
        return this;
    }

    public ConfigurationAsso objet(String objet) {
        this.setObjet(objet);
        return this;
    }

    public ConfigurationAsso objet1(String objet1) {
        this.setObjet1(objet1);
        return this;
    }

    public ConfigurationAsso objet2(String objet2) {
        this.setObjet2(objet2);
        return this;
    }

    public ConfigurationAsso adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public ConfigurationAsso siret(String objet2) {
        this.setSiret(objet2);
        return this;
    }

    public ConfigurationAsso email(String objet2) {
        this.setEmail(objet2);
        return this;
    }

    public ConfigurationAsso telephone(String objet2) {
        this.setTelephone(objet2);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
