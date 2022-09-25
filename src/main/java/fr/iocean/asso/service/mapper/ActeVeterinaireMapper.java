package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.ActeVeterinaire;
import fr.iocean.asso.domain.VisiteVeterinaire;
import fr.iocean.asso.service.dto.ActeVeterinaireDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link ActeVeterinaire} and its DTO
 * {@link ActeVeterinaireDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActeVeterinaireMapper extends EntityMapper<ActeVeterinaireDTO, ActeVeterinaire> {
    @Mapping(target = "visiteVeterinaireId", source = "visiteVeterinaire.id")
    ActeVeterinaireDTO toDto(ActeVeterinaire s);

    @Mapping(target = "visiteVeterinaire", source = "visiteVeterinaireId")
    ActeVeterinaire toEntity(ActeVeterinaireDTO s);

    default VisiteVeterinaire fromVisiteVeterinaireId(Long id) {
        if (id == null) {
            return null;
        }
        VisiteVeterinaire visiteVeterinaire = new VisiteVeterinaire();
        visiteVeterinaire.setId(id);
        return visiteVeterinaire;
    }
}
