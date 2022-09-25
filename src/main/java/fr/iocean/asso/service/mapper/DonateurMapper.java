package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Donateur;
import fr.iocean.asso.service.dto.DonateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Donateur} and its DTO {@link DonateurDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface DonateurMapper extends EntityMapper<DonateurDTO, Donateur> {}
