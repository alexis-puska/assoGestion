package fr.iocean.asso.service;

import fr.iocean.asso.domain.PointCapture;
import fr.iocean.asso.repository.PointCaptureRepository;
import fr.iocean.asso.service.dto.PointCaptureDTO;
import fr.iocean.asso.service.mapper.PointCaptureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PointCapture}.
 */
@Service
@Transactional
public class PointCaptureService {

    private final Logger log = LoggerFactory.getLogger(PointCaptureService.class);

    private final PointCaptureRepository pointCaptureRepository;

    private final PointCaptureMapper pointCaptureMapper;

    public PointCaptureService(PointCaptureRepository pointCaptureRepository, PointCaptureMapper pointCaptureMapper) {
        this.pointCaptureRepository = pointCaptureRepository;
        this.pointCaptureMapper = pointCaptureMapper;
    }

    /**
     * Save a pointCapture.
     *
     * @param pointCaptureDTO the entity to save.
     * @return the persisted entity.
     */
    public PointCaptureDTO save(PointCaptureDTO pointCaptureDTO) {
        log.debug("Request to save PointCapture : {}", pointCaptureDTO);
        PointCapture pointCapture = pointCaptureMapper.toEntity(pointCaptureDTO);
        pointCapture = pointCaptureRepository.save(pointCapture);
        return pointCaptureMapper.toDto(pointCapture);
    }

    /**
     * Partially update a pointCapture.
     *
     * @param pointCaptureDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PointCaptureDTO> partialUpdate(PointCaptureDTO pointCaptureDTO) {
        log.debug("Request to partially update PointCapture : {}", pointCaptureDTO);

        return pointCaptureRepository
            .findById(pointCaptureDTO.getId())
            .map(existingPointCapture -> {
                pointCaptureMapper.partialUpdate(existingPointCapture, pointCaptureDTO);

                return existingPointCapture;
            })
            .map(pointCaptureRepository::save)
            .map(pointCaptureMapper::toDto);
    }

    /**
     * Get all the pointCaptures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PointCaptureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PointCaptures");
        return pointCaptureRepository.findAll(pageable).map(pointCaptureMapper::toDto);
    }

    /**
     * Get one pointCapture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PointCaptureDTO> findOne(Long id) {
        log.debug("Request to get PointCapture : {}", id);
        return pointCaptureRepository.findById(id).map(pointCaptureMapper::toDto);
    }

    /**
     * Delete the pointCapture by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PointCapture : {}", id);
        pointCaptureRepository.deleteById(id);
    }
}
