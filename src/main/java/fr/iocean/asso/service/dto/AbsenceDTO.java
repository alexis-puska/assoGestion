package fr.iocean.asso.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.Absence} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class AbsenceDTO implements Serializable {

    private static final long serialVersionUID = -2980715605667471127L;

    private Long id;

    @Size(max = 512)
    private String motif;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;

    private UserLightDTO user;
}
