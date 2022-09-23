package fr.iocean.asso.service;

import fr.iocean.asso.domain.FamilleAccueil;
import fr.iocean.asso.repository.FamilleAccueilRepository;
import fr.iocean.asso.service.dto.FamilleAccueilDTO;
import fr.iocean.asso.service.mapper.FamilleAccueilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FamilleAccueil}.
 */
@Service
@Transactional
public class FamilleAccueilService {

    private final Logger log = LoggerFactory.getLogger(FamilleAccueilService.class);

    private final FamilleAccueilRepository familleAccueilRepository;

    private final FamilleAccueilMapper familleAccueilMapper;

    public FamilleAccueilService(FamilleAccueilRepository familleAccueilRepository, FamilleAccueilMapper familleAccueilMapper) {
        this.familleAccueilRepository = familleAccueilRepository;
        this.familleAccueilMapper = familleAccueilMapper;
    }

    /**
     * Save a familleAccueil.
     *
     * @param familleAccueilDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilleAccueilDTO save(FamilleAccueilDTO familleAccueilDTO) {
        log.debug("Request to save FamilleAccueil : {}", familleAccueilDTO);
        FamilleAccueil familleAccueil = familleAccueilMapper.toEntity(familleAccueilDTO);
        familleAccueil = familleAccueilRepository.save(familleAccueil);
        return familleAccueilMapper.toDto(familleAccueil);
    }

    /**
     * Partially update a familleAccueil.
     *
     * @param familleAccueilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FamilleAccueilDTO> partialUpdate(FamilleAccueilDTO familleAccueilDTO) {
        log.debug("Request to partially update FamilleAccueil : {}", familleAccueilDTO);

        return familleAccueilRepository
            .findById(familleAccueilDTO.getId())
            .map(existingFamilleAccueil -> {
                familleAccueilMapper.partialUpdate(existingFamilleAccueil, familleAccueilDTO);

                return existingFamilleAccueil;
            })
            .map(familleAccueilRepository::save)
            .map(familleAccueilMapper::toDto);
    }

    /**
     * Get all the familleAccueils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilleAccueilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FamilleAccueils");
        return familleAccueilRepository.findAll(pageable).map(familleAccueilMapper::toDto);
    }

    /**
     * Get one familleAccueil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FamilleAccueilDTO> findOne(Long id) {
        log.debug("Request to get FamilleAccueil : {}", id);
        return familleAccueilRepository.findById(id).map(familleAccueilMapper::toDto);
    }

    /**
     * Delete the familleAccueil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FamilleAccueil : {}", id);
        familleAccueilRepository.deleteById(id);
    }
}
