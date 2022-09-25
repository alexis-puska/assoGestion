package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Contact;
import fr.iocean.asso.domain.PointNourrissage;
import fr.iocean.asso.service.dto.PointNourrissageDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link PointNourrissage} and its DTO {@link PointNourrissageDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class, ContactMapper.class })
public interface PointNourrissageMapper extends EntityMapper<PointNourrissageDTO, PointNourrissage> {
    @AfterMapping
    default void after(@MappingTarget PointNourrissage pointNourrissage) {
        if (pointNourrissage.getContacts() != null) {
            for (Contact contact : pointNourrissage.getContacts()) {
                contact.setPointNourrissage(pointNourrissage);
                contact.setFamilleAccueil(null);
            }
        }
    }
}
