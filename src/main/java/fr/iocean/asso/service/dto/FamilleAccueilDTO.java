package fr.iocean.asso.service.dto;

import fr.iocean.asso.domain.enumeration.TypeLogementEnum;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.FamilleAccueil} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class FamilleAccueilDTO implements Serializable {

    private static final long serialVersionUID = 7463116285893555288L;

    private Long id;

    private String nom;

    private TypeLogementEnum typeLogement;

    private Integer nombrePiece;

    private Integer nombreChat;

    private Integer nombreChien;

    private AdresseDTO adresse;
}
