package fr.iocean.asso.service.dto;

import fr.iocean.asso.domain.enumeration.FormeDonEnum;
import fr.iocean.asso.domain.enumeration.NatureDon;
import fr.iocean.asso.domain.enumeration.NumeraireDonEnum;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.Donateur} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class DonateurDTO implements Serializable {

    private static final long serialVersionUID = 1895274399380620405L;

    private Long id;

    private String nom;

    private String prenom;

    private Double montant;

    private String sommeTouteLettre;

    private FormeDonEnum formeDon;

    private NatureDon natureDon;

    private NumeraireDonEnum numeraireDon;

    private AdresseDTO adresse;
}
