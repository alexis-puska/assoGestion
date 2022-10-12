package fr.iocean.asso.service.dto;

import fr.iocean.asso.domain.enumeration.ActeVeterinaireEnum;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.ActeVeterinaire} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class ActeVeterinaireDTO implements Serializable {

    private static final long serialVersionUID = -476654019654126327L;

    private Long id;

    private ActeVeterinaireEnum libelle;

    private long visiteVeterinaireId;
}
