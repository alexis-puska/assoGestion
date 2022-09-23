package fr.iocean.asso.service;

import fr.iocean.asso.domain.VisiteVeterinaire;
import fr.iocean.asso.repository.VisiteVeterinaireRepository;
import fr.iocean.asso.service.dto.VisiteVeterinaireDTO;
import fr.iocean.asso.service.mapper.VisiteVeterinaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VisiteVeterinaire}.
 */
@Service
@Transactional
public class VisiteVeterinaireService {

    private final Logger log = LoggerFactory.getLogger(VisiteVeterinaireService.class);

    private final VisiteVeterinaireRepository visiteVeterinaireRepository;

    private final VisiteVeterinaireMapper visiteVeterinaireMapper;

    public VisiteVeterinaireService(
        VisiteVeterinaireRepository visiteVeterinaireRepository,
        VisiteVeterinaireMapper visiteVeterinaireMapper
    ) {
        this.visiteVeterinaireRepository = visiteVeterinaireRepository;
        this.visiteVeterinaireMapper = visiteVeterinaireMapper;
    }

    /**
     * Save a visiteVeterinaire.
     *
     * @param visiteVeterinaireDTO the entity to save.
     * @return the persisted entity.
     */
    public VisiteVeterinaireDTO save(VisiteVeterinaireDTO visiteVeterinaireDTO) {
        log.debug("Request to save VisiteVeterinaire : {}", visiteVeterinaireDTO);
        VisiteVeterinaire visiteVeterinaire = visiteVeterinaireMapper.toEntity(visiteVeterinaireDTO);
        visiteVeterinaire = visiteVeterinaireRepository.save(visiteVeterinaire);
        return visiteVeterinaireMapper.toDto(visiteVeterinaire);
    }

    /**
     * Partially update a visiteVeterinaire.
     *
     * @param visiteVeterinaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VisiteVeterinaireDTO> partialUpdate(VisiteVeterinaireDTO visiteVeterinaireDTO) {
        log.debug("Request to partially update VisiteVeterinaire : {}", visiteVeterinaireDTO);

        return visiteVeterinaireRepository
            .findById(visiteVeterinaireDTO.getId())
            .map(existingVisiteVeterinaire -> {
                visiteVeterinaireMapper.partialUpdate(existingVisiteVeterinaire, visiteVeterinaireDTO);

                return existingVisiteVeterinaire;
            })
            .map(visiteVeterinaireRepository::save)
            .map(visiteVeterinaireMapper::toDto);
    }

    /**
     * Get all the visiteVeterinaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VisiteVeterinaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VisiteVeterinaires");
        return visiteVeterinaireRepository.findAll(pageable).map(visiteVeterinaireMapper::toDto);
    }

    /**
     * Get one visiteVeterinaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VisiteVeterinaireDTO> findOne(Long id) {
        log.debug("Request to get VisiteVeterinaire : {}", id);
        return visiteVeterinaireRepository.findById(id).map(visiteVeterinaireMapper::toDto);
    }

    /**
     * Delete the visiteVeterinaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VisiteVeterinaire : {}", id);
        visiteVeterinaireRepository.deleteById(id);
    }
}
