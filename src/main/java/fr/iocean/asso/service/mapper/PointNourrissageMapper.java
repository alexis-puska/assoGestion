package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.PointNourrissage;
import fr.iocean.asso.service.dto.PointNourrissageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PointNourrissage} and its DTO {@link PointNourrissageDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface PointNourrissageMapper extends EntityMapper<PointNourrissageDTO, PointNourrissage> {
    @Mapping(target = "adresse", source = "adresse", qualifiedByName = "id")
    PointNourrissageDTO toDto(PointNourrissage s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PointNourrissageDTO toDtoId(PointNourrissage pointNourrissage);
}
