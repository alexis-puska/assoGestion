package fr.iocean.asso.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.ConfigurationDon} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class ConfigurationDonDTO implements Serializable {

    private static final long serialVersionUID = -1178494128525966966L;

    private Long id;

    private String denomination;

    private String objet;

    private String signataire;

    private AdresseDTO adresse;
}
