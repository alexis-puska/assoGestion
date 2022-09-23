package fr.iocean.asso.service;

import fr.iocean.asso.domain.CliniqueVeterinaire;
import fr.iocean.asso.repository.CliniqueVeterinaireRepository;
import fr.iocean.asso.service.dto.CliniqueVeterinaireDTO;
import fr.iocean.asso.service.mapper.CliniqueVeterinaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CliniqueVeterinaire}.
 */
@Service
@Transactional
public class CliniqueVeterinaireService {

    private final Logger log = LoggerFactory.getLogger(CliniqueVeterinaireService.class);

    private final CliniqueVeterinaireRepository cliniqueVeterinaireRepository;

    private final CliniqueVeterinaireMapper cliniqueVeterinaireMapper;

    public CliniqueVeterinaireService(
        CliniqueVeterinaireRepository cliniqueVeterinaireRepository,
        CliniqueVeterinaireMapper cliniqueVeterinaireMapper
    ) {
        this.cliniqueVeterinaireRepository = cliniqueVeterinaireRepository;
        this.cliniqueVeterinaireMapper = cliniqueVeterinaireMapper;
    }

    /**
     * Save a cliniqueVeterinaire.
     *
     * @param cliniqueVeterinaireDTO the entity to save.
     * @return the persisted entity.
     */
    public CliniqueVeterinaireDTO save(CliniqueVeterinaireDTO cliniqueVeterinaireDTO) {
        log.debug("Request to save CliniqueVeterinaire : {}", cliniqueVeterinaireDTO);
        CliniqueVeterinaire cliniqueVeterinaire = cliniqueVeterinaireMapper.toEntity(cliniqueVeterinaireDTO);
        cliniqueVeterinaire = cliniqueVeterinaireRepository.save(cliniqueVeterinaire);
        return cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);
    }

    /**
     * Partially update a cliniqueVeterinaire.
     *
     * @param cliniqueVeterinaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CliniqueVeterinaireDTO> partialUpdate(CliniqueVeterinaireDTO cliniqueVeterinaireDTO) {
        log.debug("Request to partially update CliniqueVeterinaire : {}", cliniqueVeterinaireDTO);

        return cliniqueVeterinaireRepository
            .findById(cliniqueVeterinaireDTO.getId())
            .map(existingCliniqueVeterinaire -> {
                cliniqueVeterinaireMapper.partialUpdate(existingCliniqueVeterinaire, cliniqueVeterinaireDTO);

                return existingCliniqueVeterinaire;
            })
            .map(cliniqueVeterinaireRepository::save)
            .map(cliniqueVeterinaireMapper::toDto);
    }

    /**
     * Get all the cliniqueVeterinaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CliniqueVeterinaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CliniqueVeterinaires");
        return cliniqueVeterinaireRepository.findAll(pageable).map(cliniqueVeterinaireMapper::toDto);
    }

    /**
     * Get one cliniqueVeterinaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CliniqueVeterinaireDTO> findOne(Long id) {
        log.debug("Request to get CliniqueVeterinaire : {}", id);
        return cliniqueVeterinaireRepository.findById(id).map(cliniqueVeterinaireMapper::toDto);
    }

    /**
     * Delete the cliniqueVeterinaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CliniqueVeterinaire : {}", id);
        cliniqueVeterinaireRepository.deleteById(id);
    }
}
