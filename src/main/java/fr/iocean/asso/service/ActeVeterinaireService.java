package fr.iocean.asso.service;

import fr.iocean.asso.domain.ActeVeterinaire;
import fr.iocean.asso.repository.ActeVeterinaireRepository;
import fr.iocean.asso.service.dto.ActeVeterinaireDTO;
import fr.iocean.asso.service.mapper.ActeVeterinaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ActeVeterinaire}.
 */
@Service
@Transactional
public class ActeVeterinaireService {

    private final Logger log = LoggerFactory.getLogger(ActeVeterinaireService.class);

    private final ActeVeterinaireRepository acteVeterinaireRepository;

    private final ActeVeterinaireMapper acteVeterinaireMapper;

    public ActeVeterinaireService(ActeVeterinaireRepository acteVeterinaireRepository, ActeVeterinaireMapper acteVeterinaireMapper) {
        this.acteVeterinaireRepository = acteVeterinaireRepository;
        this.acteVeterinaireMapper = acteVeterinaireMapper;
    }

    /**
     * Save a acteVeterinaire.
     *
     * @param acteVeterinaireDTO the entity to save.
     * @return the persisted entity.
     */
    public ActeVeterinaireDTO save(ActeVeterinaireDTO acteVeterinaireDTO) {
        log.debug("Request to save ActeVeterinaire : {}", acteVeterinaireDTO);
        ActeVeterinaire acteVeterinaire = acteVeterinaireMapper.toEntity(acteVeterinaireDTO);
        acteVeterinaire = acteVeterinaireRepository.save(acteVeterinaire);
        return acteVeterinaireMapper.toDto(acteVeterinaire);
    }

    /**
     * Partially update a acteVeterinaire.
     *
     * @param acteVeterinaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ActeVeterinaireDTO> partialUpdate(ActeVeterinaireDTO acteVeterinaireDTO) {
        log.debug("Request to partially update ActeVeterinaire : {}", acteVeterinaireDTO);

        return acteVeterinaireRepository
            .findById(acteVeterinaireDTO.getId())
            .map(existingActeVeterinaire -> {
                acteVeterinaireMapper.partialUpdate(existingActeVeterinaire, acteVeterinaireDTO);

                return existingActeVeterinaire;
            })
            .map(acteVeterinaireRepository::save)
            .map(acteVeterinaireMapper::toDto);
    }

    /**
     * Get all the acteVeterinaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ActeVeterinaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActeVeterinaires");
        return acteVeterinaireRepository.findAll(pageable).map(acteVeterinaireMapper::toDto);
    }

    /**
     * Get one acteVeterinaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActeVeterinaireDTO> findOne(Long id) {
        log.debug("Request to get ActeVeterinaire : {}", id);
        return acteVeterinaireRepository.findById(id).map(acteVeterinaireMapper::toDto);
    }

    /**
     * Delete the acteVeterinaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ActeVeterinaire : {}", id);
        acteVeterinaireRepository.deleteById(id);
    }
}
