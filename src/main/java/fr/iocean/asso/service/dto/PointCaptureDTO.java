package fr.iocean.asso.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.PointCapture} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class PointCaptureDTO implements Serializable {

    private static final long serialVersionUID = 4653573818169526561L;

    private Long id;

    private String nom;

    private AdresseDTO adresseCapture;
}
