package fr.iocean.asso.service.dto;

import fr.iocean.asso.domain.enumeration.PoilEnum;
import fr.iocean.asso.domain.enumeration.TypeIdentificationEnum;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.Chat} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatDTO implements Serializable {

    private static final long serialVersionUID = -7017586701182618685L;

    private Long id;

    @NotNull
    private String nom;

    private TypeIdentificationEnum typeIdentification;

    private String identification;

    @NotNull
    private LocalDate dateNaissance;

    @Lob
    private String description;

    @NotNull
    private String robe;

    @NotNull
    private PoilEnum poil;

    private ContratDTO contrat;

    private FamilleAccueilDTO famille;

    private PointCaptureDTO adresseCapture;

    private RaceChatDTO race;
}
