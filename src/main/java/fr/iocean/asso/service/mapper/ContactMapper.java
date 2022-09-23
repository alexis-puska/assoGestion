package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Contact;
import fr.iocean.asso.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring", uses = { FamilleAccueilMapper.class, PointNourrissageMapper.class })
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "familleAccueil", source = "familleAccueil", qualifiedByName = "id")
    @Mapping(target = "pointNourrissage", source = "pointNourrissage", qualifiedByName = "id")
    ContactDTO toDto(Contact s);
}
