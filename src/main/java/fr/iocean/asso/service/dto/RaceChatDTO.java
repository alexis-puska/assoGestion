package fr.iocean.asso.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.RaceChat} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class RaceChatDTO implements Serializable {

    private static final long serialVersionUID = -8834084882185467116L;

    private Long id;

    private String libelle;
}
