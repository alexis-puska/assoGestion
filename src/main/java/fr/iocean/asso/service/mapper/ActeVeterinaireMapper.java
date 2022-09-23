package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.ActeVeterinaire;
import fr.iocean.asso.service.dto.ActeVeterinaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ActeVeterinaire} and its DTO {@link ActeVeterinaireDTO}.
 */
@Mapper(componentModel = "spring", uses = { VisiteVeterinaireMapper.class })
public interface ActeVeterinaireMapper extends EntityMapper<ActeVeterinaireDTO, ActeVeterinaire> {
    @Mapping(target = "visiteVeterinaire", source = "visiteVeterinaire", qualifiedByName = "id")
    ActeVeterinaireDTO toDto(ActeVeterinaire s);
}
