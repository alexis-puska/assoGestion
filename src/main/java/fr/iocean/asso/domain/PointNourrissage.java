package fr.iocean.asso.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
 * A PointNourrissage.
 */
@Entity
@Table(name = "point_nourrissage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PointNourrissage implements Serializable {

    private static final long serialVersionUID = -2081636171828844931L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Adresse adresse;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        joinColumns = @JoinColumn(name = "point_nourrissage_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        indexes = {
            @javax.persistence.Index(columnList = "point_nourrissage_id", name = "point_nourrissage_id_ind"),
            @javax.persistence.Index(columnList = "user_id", name = "user_pn_id_ind"),
        }
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PointNourrissage id(Long id) {
        this.setId(id);
        return this;
    }

    public PointNourrissage nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public PointNourrissage adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public PointNourrissage contacts(Set<User> contacts) {
        this.setContacts(contacts);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here
}
