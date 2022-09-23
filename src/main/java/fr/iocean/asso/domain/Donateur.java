package fr.iocean.asso.domain;

import fr.iocean.asso.domain.enumeration.FormeDonEnum;
import fr.iocean.asso.domain.enumeration.NatureDon;
import fr.iocean.asso.domain.enumeration.NumeraireDonEnum;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * A Donateur.
 */
@Entity
@Table(name = "donateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Donateur implements Serializable {

    private static final long serialVersionUID = -5264351149858384852L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "somme_toute_lettre")
    private String sommeTouteLettre;

    @Enumerated(EnumType.STRING)
    @Column(name = "forme_don")
    private FormeDonEnum formeDon;

    @Enumerated(EnumType.STRING)
    @Column(name = "nature_don")
    private NatureDon natureDon;

    @Enumerated(EnumType.STRING)
    @Column(name = "numeraire_don")
    private NumeraireDonEnum numeraireDon;

    @OneToOne
    @JoinColumn(unique = true)
    private Adresse adresse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Donateur id(Long id) {
        this.setId(id);
        return this;
    }

    public Donateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public Donateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public Donateur montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public Donateur sommeTouteLettre(String sommeTouteLettre) {
        this.setSommeTouteLettre(sommeTouteLettre);
        return this;
    }

    public Donateur formeDon(FormeDonEnum formeDon) {
        this.setFormeDon(formeDon);
        return this;
    }

    public Donateur natureDon(NatureDon natureDon) {
        this.setNatureDon(natureDon);
        return this;
    }

    public Donateur numeraireDon(NumeraireDonEnum numeraireDon) {
        this.setNumeraireDon(numeraireDon);
        return this;
    }

    public Donateur adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
