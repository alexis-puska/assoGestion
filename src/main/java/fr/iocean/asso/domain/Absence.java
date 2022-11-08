package fr.iocean.asso.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Absence.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "absence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Absence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 512)
    @Column(name = "motif", length = 512)
    private String motif;

    @NotNull
    @Column(name = "start", nullable = false)
    private LocalDate start;

    @NotNull
    @Column(name = "jhi_end", nullable = false)
    private LocalDate end;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Absence id(Long id) {
        this.setId(id);
        return this;
    }

    public Absence motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public Absence start(LocalDate start) {
        this.setStart(start);
        return this;
    }

    public Absence end(LocalDate end) {
        this.setEnd(end);
        return this;
    }

    public Absence user(User user) {
        this.setUser(user);
        return this;
    }
}
