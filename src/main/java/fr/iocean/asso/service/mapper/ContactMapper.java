package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Contact;
import fr.iocean.asso.domain.FamilleAccueil;
import fr.iocean.asso.domain.PointNourrissage;
import fr.iocean.asso.service.dto.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "familleAccueilId", source = "familleAccueil.id")
    @Mapping(target = "pointNourrissageId", source = "pointNourrissage.id")
    ContactDTO toDto(Contact s);

    @Mapping(target = "familleAccueil", source = "familleAccueilId")
    @Mapping(target = "pointNourrissage", source = "pointNourrissageId")
    Contact toEntity(ContactDTO s);

    default FamilleAccueil fromFamilleAcceuilId(Long id) {
        if (id == null) {
            return null;
        }
        FamilleAccueil familleAccueil = new FamilleAccueil();
        familleAccueil.setId(id);
        return familleAccueil;
    }

    default PointNourrissage fromPointNourrissageId(Long id) {
        if (id == null) {
            return null;
        }
        PointNourrissage pointNourrissage = new PointNourrissage();
        pointNourrissage.setId(id);
        return pointNourrissage;
    }
}
