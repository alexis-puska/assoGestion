package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.VisiteVeterinaire;
import fr.iocean.asso.service.dto.VisiteVeterinaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VisiteVeterinaire} and its DTO {@link VisiteVeterinaireDTO}.
 */
@Mapper(componentModel = "spring", uses = { CliniqueVeterinaireMapper.class, ChatMapper.class })
public interface VisiteVeterinaireMapper extends EntityMapper<VisiteVeterinaireDTO, VisiteVeterinaire> {
    @Mapping(target = "cliniqueVeterinaire", source = "cliniqueVeterinaire", qualifiedByName = "id")
    @Mapping(target = "chat", source = "chat", qualifiedByName = "id")
    VisiteVeterinaireDTO toDto(VisiteVeterinaire s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VisiteVeterinaireDTO toDtoId(VisiteVeterinaire visiteVeterinaire);
}
