package fr.iocean.asso.domain;

import fr.iocean.asso.domain.enumeration.TypeLogementEnum;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
 * A FamilleAccueil.
 */
@Entity
@Table(name = "famille_accueil")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FamilleAccueil implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4852560220192011859L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_logement")
    private TypeLogementEnum typeLogement;

    @Column(name = "nombre_piece")
    private Integer nombrePiece;

    @Column(name = "nombre_chat")
    private Integer nombreChat;

    @Column(name = "nombre_chien")
    private Integer nombreChien;

    @OneToOne
    @JoinColumn(unique = true)
    private Adresse adresse;

    @OneToMany(mappedBy = "familleAccueil")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FamilleAccueil id(Long id) {
        this.setId(id);
        return this;
    }

    public FamilleAccueil nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public FamilleAccueil typeLogement(TypeLogementEnum typeLogement) {
        this.setTypeLogement(typeLogement);
        return this;
    }

    public FamilleAccueil nombrePiece(Integer nombrePiece) {
        this.setNombrePiece(nombrePiece);
        return this;
    }

    public FamilleAccueil nombreChat(Integer nombreChat) {
        this.setNombreChat(nombreChat);
        return this;
    }

    public FamilleAccueil nombreChien(Integer nombreChien) {
        this.setNombreChien(nombreChien);
        return this;
    }

    public FamilleAccueil adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public FamilleAccueil contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
