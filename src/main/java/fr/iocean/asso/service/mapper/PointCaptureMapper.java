package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.PointCapture;
import fr.iocean.asso.service.dto.PointCaptureDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link PointCapture} and its DTO
 * {@link PointCaptureDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface PointCaptureMapper extends EntityMapper<PointCaptureDTO, PointCapture> {}
