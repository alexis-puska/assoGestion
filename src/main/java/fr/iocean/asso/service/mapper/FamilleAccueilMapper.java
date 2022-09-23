package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.FamilleAccueil;
import fr.iocean.asso.service.dto.FamilleAccueilDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FamilleAccueil} and its DTO {@link FamilleAccueilDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface FamilleAccueilMapper extends EntityMapper<FamilleAccueilDTO, FamilleAccueil> {
    @Mapping(target = "adresse", source = "adresse", qualifiedByName = "id")
    FamilleAccueilDTO toDto(FamilleAccueil s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FamilleAccueilDTO toDtoId(FamilleAccueil familleAccueil);
}
