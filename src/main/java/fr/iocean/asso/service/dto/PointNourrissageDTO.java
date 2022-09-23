package fr.iocean.asso.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.PointNourrissage} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class PointNourrissageDTO implements Serializable {

    private static final long serialVersionUID = -6106393308768672382L;

    private Long id;

    private String nom;

    private AdresseDTO adresse;
}
