package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Adresse;
import fr.iocean.asso.service.dto.AdresseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Adresse} and its DTO {@link AdresseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AdresseMapper extends EntityMapper<AdresseDTO, Adresse> {}
