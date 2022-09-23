package fr.iocean.asso.service;

import fr.iocean.asso.domain.Donateur;
import fr.iocean.asso.repository.DonateurRepository;
import fr.iocean.asso.service.dto.DonateurDTO;
import fr.iocean.asso.service.mapper.DonateurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Donateur}.
 */
@Service
@Transactional
public class DonateurService {

    private final Logger log = LoggerFactory.getLogger(DonateurService.class);

    private final DonateurRepository donateurRepository;

    private final DonateurMapper donateurMapper;

    public DonateurService(DonateurRepository donateurRepository, DonateurMapper donateurMapper) {
        this.donateurRepository = donateurRepository;
        this.donateurMapper = donateurMapper;
    }

    /**
     * Save a donateur.
     *
     * @param donateurDTO the entity to save.
     * @return the persisted entity.
     */
    public DonateurDTO save(DonateurDTO donateurDTO) {
        log.debug("Request to save Donateur : {}", donateurDTO);
        Donateur donateur = donateurMapper.toEntity(donateurDTO);
        donateur = donateurRepository.save(donateur);
        return donateurMapper.toDto(donateur);
    }

    /**
     * Partially update a donateur.
     *
     * @param donateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DonateurDTO> partialUpdate(DonateurDTO donateurDTO) {
        log.debug("Request to partially update Donateur : {}", donateurDTO);

        return donateurRepository
            .findById(donateurDTO.getId())
            .map(existingDonateur -> {
                donateurMapper.partialUpdate(existingDonateur, donateurDTO);

                return existingDonateur;
            })
            .map(donateurRepository::save)
            .map(donateurMapper::toDto);
    }

    /**
     * Get all the donateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DonateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Donateurs");
        return donateurRepository.findAll(pageable).map(donateurMapper::toDto);
    }

    /**
     * Get one donateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DonateurDTO> findOne(Long id) {
        log.debug("Request to get Donateur : {}", id);
        return donateurRepository.findById(id).map(donateurMapper::toDto);
    }

    /**
     * Delete the donateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Donateur : {}", id);
        donateurRepository.deleteById(id);
    }
}
