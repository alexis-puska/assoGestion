package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.ConfigurationDon;
import fr.iocean.asso.service.dto.ConfigurationDonDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ConfigurationDon} and its DTO
 * {@link ConfigurationDonDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface ConfigurationDonMapper extends EntityMapper<ConfigurationDonDTO, ConfigurationDon> {
    ConfigurationDonDTO toDto(ConfigurationDon s);
}
