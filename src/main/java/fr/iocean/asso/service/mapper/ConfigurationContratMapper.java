package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.ConfigurationContrat;
import fr.iocean.asso.service.dto.ConfigurationContratDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigurationContrat} and its DTO {@link ConfigurationContratDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigurationContratMapper extends EntityMapper<ConfigurationContratDTO, ConfigurationContrat> {}
