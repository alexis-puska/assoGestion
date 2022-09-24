package fr.iocean.asso.domain;

import fr.iocean.asso.domain.enumeration.PaiementEnum;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * A Contrat.
 */
@Entity
@Table(name = "contrat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Contrat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "cout")
    private Double cout;

    @Enumerated(EnumType.STRING)
    @Column(name = "paiement")
    private PaiementEnum paiement;

    @Column(name = "date_contrat")
    private LocalDate dateContrat;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Adresse adresseAdoptant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Contrat id(Long id) {
        this.setId(id);
        return this;
    }

    public Contrat nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public Contrat prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public Contrat cout(Double cout) {
        this.setCout(cout);
        return this;
    }

    public Contrat paiement(PaiementEnum paiement) {
        this.setPaiement(paiement);
        return this;
    }

    public Contrat dateContrat(LocalDate dateContrat) {
        this.setDateContrat(dateContrat);
        return this;
    }

    public Contrat adresseAdoptant(Adresse adresse) {
        this.setAdresseAdoptant(adresse);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
