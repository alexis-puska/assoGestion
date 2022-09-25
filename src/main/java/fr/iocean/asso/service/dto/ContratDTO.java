package fr.iocean.asso.service.dto;

import fr.iocean.asso.domain.enumeration.PaiementEnum;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.Contrat} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class ContratDTO implements Serializable {

    private static final long serialVersionUID = 3319602062852581238L;

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    @NotNull
    private Double cout;

    @NotNull
    private PaiementEnum paiement;

    @NotNull
    private LocalDate dateContrat;

    @NotNull
    private AdresseDTO adresseAdoptant;
}
