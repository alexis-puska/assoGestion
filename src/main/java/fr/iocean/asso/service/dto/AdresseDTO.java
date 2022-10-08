package fr.iocean.asso.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.Adresse} entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdresseDTO implements Serializable {

    private static final long serialVersionUID = -7186996202518592787L;

    private Long id;

    @NotNull
    private Integer numero;

    @NotNull
    private String rue;

    @NotNull
    private String codePostale;

    @NotNull
    private String ville;
}
