package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.CliniqueVeterinaire;
import fr.iocean.asso.service.dto.CliniqueVeterinaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CliniqueVeterinaire} and its DTO {@link CliniqueVeterinaireDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface CliniqueVeterinaireMapper extends EntityMapper<CliniqueVeterinaireDTO, CliniqueVeterinaire> {
    @Mapping(target = "adresse", source = "adresse", qualifiedByName = "id")
    CliniqueVeterinaireDTO toDto(CliniqueVeterinaire s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CliniqueVeterinaireDTO toDtoId(CliniqueVeterinaire cliniqueVeterinaire);
}
