package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Contrat;
import fr.iocean.asso.service.dto.ContratDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contrat} and its DTO {@link ContratDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface ContratMapper extends EntityMapper<ContratDTO, Contrat> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContratDTO toDtoId(Contrat contrat);
}
