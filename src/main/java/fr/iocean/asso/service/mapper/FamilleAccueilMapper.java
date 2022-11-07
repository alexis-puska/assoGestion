package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.FamilleAccueil;
import fr.iocean.asso.service.dto.FamilleAccueilDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link FamilleAccueil} and its DTO
 * {@link FamilleAccueilDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class, UserMapper.class })
public interface FamilleAccueilMapper extends EntityMapper<FamilleAccueilDTO, FamilleAccueil> {}
