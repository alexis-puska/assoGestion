package fr.iocean.asso.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.Contact} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class ContactDTO implements Serializable {

    private static final long serialVersionUID = 2173473092357413781L;

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    private String mail;

    private String telMobile;

    private String telFixe;

    private Long familleAccueilId;

    private Long pointNourrissageId;
}
