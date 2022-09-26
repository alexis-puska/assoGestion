package fr.iocean.asso.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.VisiteVeterinaire} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class VisiteVeterinaireDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4444501872449510477L;

    private Long id;

    private LocalDate dateVisite;

    private CliniqueVeterinaireDTO cliniqueVeterinaire;

    private Long chatId;

    private Set<ActeVeterinaireDTO> actes = new HashSet<>();
}
