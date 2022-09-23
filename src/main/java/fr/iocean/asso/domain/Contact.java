package fr.iocean.asso.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
 * A Contact.
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Contact implements Serializable {

    private static final long serialVersionUID = 4421469563359696801L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "mail")
    private String mail;

    @Column(name = "tel_mobile")
    private String telMobile;

    @Column(name = "tel_fixe")
    private String telFixe;

    @ManyToOne
    private FamilleAccueil familleAccueil;

    @ManyToOne
    private PointNourrissage pointNourrissage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Contact id(Long id) {
        this.setId(id);
        return this;
    }

    public Contact nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public Contact prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public Contact mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public Contact telMobile(String telMobile) {
        this.setTelMobile(telMobile);
        return this;
    }

    public Contact telFixe(String telFixe) {
        this.setTelFixe(telFixe);
        return this;
    }

    public Contact familleAccueil(FamilleAccueil familleAccueil) {
        this.setFamilleAccueil(familleAccueil);
        return this;
    }

    public Contact pointNourrissage(PointNourrissage pointNourrissage) {
        this.setPointNourrissage(pointNourrissage);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here
}
