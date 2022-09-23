package fr.iocean.asso.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.asso.domain.ConfigurationContrat} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class ConfigurationContratDTO implements Serializable {

    private static final long serialVersionUID = -667212908056506892L;

    private Long id;

    private String content;
}
