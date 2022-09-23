package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.PointCapture;
import fr.iocean.asso.service.dto.PointCaptureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PointCapture} and its DTO {@link PointCaptureDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface PointCaptureMapper extends EntityMapper<PointCaptureDTO, PointCapture> {
    @Mapping(target = "adresseCapture", source = "adresseCapture", qualifiedByName = "id")
    PointCaptureDTO toDto(PointCapture s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PointCaptureDTO toDtoId(PointCapture pointCapture);
}
