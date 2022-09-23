package fr.iocean.asso.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.iocean.asso.domain.Contact} entity.
 */
public class ContactDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    private String mail;

    private String telMobile;

    private String telFixe;

    private FamilleAccueilDTO familleAccueil;

    private PointNourrissageDTO pointNourrissage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelMobile() {
        return telMobile;
    }

    public void setTelMobile(String telMobile) {
        this.telMobile = telMobile;
    }

    public String getTelFixe() {
        return telFixe;
    }

    public void setTelFixe(String telFixe) {
        this.telFixe = telFixe;
    }

    public FamilleAccueilDTO getFamilleAccueil() {
        return familleAccueil;
    }

    public void setFamilleAccueil(FamilleAccueilDTO familleAccueil) {
        this.familleAccueil = familleAccueil;
    }

    public PointNourrissageDTO getPointNourrissage() {
        return pointNourrissage;
    }

    public void setPointNourrissage(PointNourrissageDTO pointNourrissage) {
        this.pointNourrissage = pointNourrissage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactDTO)) {
            return false;
        }

        ContactDTO contactDTO = (ContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", mail='" + getMail() + "'" +
            ", telMobile='" + getTelMobile() + "'" +
            ", telFixe='" + getTelFixe() + "'" +
            ", familleAccueil=" + getFamilleAccueil() +
            ", pointNourrissage=" + getPointNourrissage() +
            "}";
    }
}
