package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.PointNourrissage;
import fr.iocean.asso.service.dto.PointNourrissageDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link PointNourrissage} and its DTO {@link PointNourrissageDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class, UserMapper.class })
public interface PointNourrissageMapper extends EntityMapper<PointNourrissageDTO, PointNourrissage> {}
