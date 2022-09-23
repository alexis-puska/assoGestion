package fr.iocean.asso.service;

import fr.iocean.asso.domain.PointNourrissage;
import fr.iocean.asso.repository.PointNourrissageRepository;
import fr.iocean.asso.service.dto.PointNourrissageDTO;
import fr.iocean.asso.service.mapper.PointNourrissageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PointNourrissage}.
 */
@Service
@Transactional
public class PointNourrissageService {

    private final Logger log = LoggerFactory.getLogger(PointNourrissageService.class);

    private final PointNourrissageRepository pointNourrissageRepository;

    private final PointNourrissageMapper pointNourrissageMapper;

    public PointNourrissageService(PointNourrissageRepository pointNourrissageRepository, PointNourrissageMapper pointNourrissageMapper) {
        this.pointNourrissageRepository = pointNourrissageRepository;
        this.pointNourrissageMapper = pointNourrissageMapper;
    }

    /**
     * Save a pointNourrissage.
     *
     * @param pointNourrissageDTO the entity to save.
     * @return the persisted entity.
     */
    public PointNourrissageDTO save(PointNourrissageDTO pointNourrissageDTO) {
        log.debug("Request to save PointNourrissage : {}", pointNourrissageDTO);
        PointNourrissage pointNourrissage = pointNourrissageMapper.toEntity(pointNourrissageDTO);
        pointNourrissage = pointNourrissageRepository.save(pointNourrissage);
        return pointNourrissageMapper.toDto(pointNourrissage);
    }

    /**
     * Partially update a pointNourrissage.
     *
     * @param pointNourrissageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PointNourrissageDTO> partialUpdate(PointNourrissageDTO pointNourrissageDTO) {
        log.debug("Request to partially update PointNourrissage : {}", pointNourrissageDTO);

        return pointNourrissageRepository
            .findById(pointNourrissageDTO.getId())
            .map(existingPointNourrissage -> {
                pointNourrissageMapper.partialUpdate(existingPointNourrissage, pointNourrissageDTO);

                return existingPointNourrissage;
            })
            .map(pointNourrissageRepository::save)
            .map(pointNourrissageMapper::toDto);
    }

    /**
     * Get all the pointNourrissages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PointNourrissageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PointNourrissages");
        return pointNourrissageRepository.findAll(pageable).map(pointNourrissageMapper::toDto);
    }

    /**
     * Get one pointNourrissage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PointNourrissageDTO> findOne(Long id) {
        log.debug("Request to get PointNourrissage : {}", id);
        return pointNourrissageRepository.findById(id).map(pointNourrissageMapper::toDto);
    }

    /**
     * Delete the pointNourrissage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PointNourrissage : {}", id);
        pointNourrissageRepository.deleteById(id);
    }
}
