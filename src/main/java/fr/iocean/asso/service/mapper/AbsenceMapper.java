package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Absence;
import fr.iocean.asso.service.dto.AbsenceDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Absence} and its DTO {@link AbsenceDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {}
