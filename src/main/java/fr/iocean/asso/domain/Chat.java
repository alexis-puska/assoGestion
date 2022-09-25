package fr.iocean.asso.domain;

import fr.iocean.asso.domain.enumeration.PoilEnum;
import fr.iocean.asso.domain.enumeration.TypeIdentificationEnum;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Chat.
 */
@Entity
@Table(name = "chat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Chat implements Serializable {

    private static final long serialVersionUID = 8923512023167407050L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_identification")
    private TypeIdentificationEnum typeIdentification;

    @Column(name = "identification")
    private String identification;

    @NotNull
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "robe", nullable = false)
    private String robe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "poil", nullable = false)
    private PoilEnum poil;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Contrat contrat;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<VisiteVeterinaire> visites = new HashSet<>();

    @ManyToOne
    private FamilleAccueil famille;

    @ManyToOne
    private PointCapture adresseCapture;

    @ManyToOne
    private RaceChat race;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Chat id(Long id) {
        this.setId(id);
        return this;
    }

    public Chat nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public Chat typeIdentification(TypeIdentificationEnum typeIdentification) {
        this.setTypeIdentification(typeIdentification);
        return this;
    }

    public Chat identification(String identification) {
        this.setIdentification(identification);
        return this;
    }

    public Chat dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public Chat description(String description) {
        this.setDescription(description);
        return this;
    }

    public Chat robe(String robe) {
        this.setRobe(robe);
        return this;
    }

    public Chat poil(PoilEnum poil) {
        this.setPoil(poil);
        return this;
    }

    public Chat contrat(Contrat contrat) {
        this.setContrat(contrat);
        return this;
    }

    public Set<VisiteVeterinaire> getVisites() {
        return this.visites;
    }

    public Chat visites(Set<VisiteVeterinaire> visiteVeterinaires) {
        this.setVisites(visiteVeterinaires);
        return this;
    }

    public Chat famille(FamilleAccueil familleAccueil) {
        this.setFamille(familleAccueil);
        return this;
    }

    public Chat adresseCapture(PointCapture pointCapture) {
        this.setAdresseCapture(pointCapture);
        return this;
    }

    public Chat race(RaceChat raceChat) {
        this.setRace(raceChat);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
