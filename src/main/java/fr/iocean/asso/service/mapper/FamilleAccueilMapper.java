package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Contact;
import fr.iocean.asso.domain.FamilleAccueil;
import fr.iocean.asso.service.dto.FamilleAccueilDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link FamilleAccueil} and its DTO
 * {@link FamilleAccueilDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class, ContactMapper.class })
public interface FamilleAccueilMapper extends EntityMapper<FamilleAccueilDTO, FamilleAccueil> {
    @AfterMapping
    default void after(@MappingTarget FamilleAccueil familleAccueil) {
        if (familleAccueil.getContacts() != null) {
            for (Contact contact : familleAccueil.getContacts()) {
                contact.setFamilleAccueil(familleAccueil);
                contact.setPointNourrissage(null);
            }
        }
    }
}
