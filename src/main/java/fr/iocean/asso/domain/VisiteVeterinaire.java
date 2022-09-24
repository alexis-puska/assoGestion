package fr.iocean.asso.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VisiteVeterinaire.
 */
@Entity
@Table(name = "visite_veterinaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class VisiteVeterinaire implements Serializable {

    private static final long serialVersionUID = 3994003078900804571L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_visite")
    private LocalDate dateVisite;

    @OneToMany(mappedBy = "visiteVeterinaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ActeVeterinaire> actes = new HashSet<>();

    @ManyToOne
    private CliniqueVeterinaire cliniqueVeterinaire;

    @ManyToOne
    private Chat chat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public VisiteVeterinaire id(Long id) {
        this.setId(id);
        return this;
    }

    public VisiteVeterinaire dateVisite(LocalDate dateVisite) {
        this.setDateVisite(dateVisite);
        return this;
    }

    public VisiteVeterinaire actes(Set<ActeVeterinaire> acteVeterinaires) {
        this.setActes(acteVeterinaires);
        return this;
    }

    public VisiteVeterinaire cliniqueVeterinaire(CliniqueVeterinaire cliniqueVeterinaire) {
        this.setCliniqueVeterinaire(cliniqueVeterinaire);
        return this;
    }

    public VisiteVeterinaire chat(Chat chat) {
        this.setChat(chat);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
