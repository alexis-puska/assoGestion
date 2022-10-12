package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.ConfigurationAsso;
import fr.iocean.asso.service.dto.ConfigurationAssoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ConfigurationAsso} and its DTO
 * {@link ConfigurationAssoDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface ConfigurationAssoMapper extends EntityMapper<ConfigurationAssoDTO, ConfigurationAsso> {
    ConfigurationAssoDTO toDto(ConfigurationAsso s);
}
