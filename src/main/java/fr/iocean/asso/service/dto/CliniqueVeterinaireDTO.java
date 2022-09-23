package fr.iocean.asso.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.CliniqueVeterinaire} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class CliniqueVeterinaireDTO implements Serializable {

    private static final long serialVersionUID = 3425075369264939801L;

    private Long id;

    private String nom;

    private Boolean actif;

    private AdresseDTO adresse;
}
