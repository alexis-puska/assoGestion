package fr.iocean.asso.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * A PointCapture.
 */
@Entity
@Table(name = "point_capture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PointCapture implements Serializable {

    private static final long serialVersionUID = 6639937520557876627L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToOne
    @JoinColumn(unique = true)
    private Adresse adresseCapture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PointCapture id(Long id) {
        this.setId(id);
        return this;
    }

    public PointCapture nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public PointCapture adresseCapture(Adresse adresse) {
        this.setAdresseCapture(adresse);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
