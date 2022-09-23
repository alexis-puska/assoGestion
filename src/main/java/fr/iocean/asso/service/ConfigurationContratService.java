package fr.iocean.asso.service;

import fr.iocean.asso.domain.ConfigurationContrat;
import fr.iocean.asso.repository.ConfigurationContratRepository;
import fr.iocean.asso.service.dto.ConfigurationContratDTO;
import fr.iocean.asso.service.mapper.ConfigurationContratMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConfigurationContrat}.
 */
@Service
@Transactional
public class ConfigurationContratService {

    private final Logger log = LoggerFactory.getLogger(ConfigurationContratService.class);

    private final ConfigurationContratRepository configurationContratRepository;

    private final ConfigurationContratMapper configurationContratMapper;

    public ConfigurationContratService(
        ConfigurationContratRepository configurationContratRepository,
        ConfigurationContratMapper configurationContratMapper
    ) {
        this.configurationContratRepository = configurationContratRepository;
        this.configurationContratMapper = configurationContratMapper;
    }

    /**
     * Save a configurationContrat.
     *
     * @param configurationContratDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfigurationContratDTO save(ConfigurationContratDTO configurationContratDTO) {
        log.debug("Request to save ConfigurationContrat : {}", configurationContratDTO);
        ConfigurationContrat configurationContrat = configurationContratMapper.toEntity(configurationContratDTO);
        configurationContrat = configurationContratRepository.save(configurationContrat);
        return configurationContratMapper.toDto(configurationContrat);
    }

    /**
     * Partially update a configurationContrat.
     *
     * @param configurationContratDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConfigurationContratDTO> partialUpdate(ConfigurationContratDTO configurationContratDTO) {
        log.debug("Request to partially update ConfigurationContrat : {}", configurationContratDTO);

        return configurationContratRepository
            .findById(configurationContratDTO.getId())
            .map(existingConfigurationContrat -> {
                configurationContratMapper.partialUpdate(existingConfigurationContrat, configurationContratDTO);

                return existingConfigurationContrat;
            })
            .map(configurationContratRepository::save)
            .map(configurationContratMapper::toDto);
    }

    /**
     * Get all the configurationContrats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationContratDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigurationContrats");
        return configurationContratRepository.findAll(pageable).map(configurationContratMapper::toDto);
    }

    /**
     * Get one configurationContrat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfigurationContratDTO> findOne(Long id) {
        log.debug("Request to get ConfigurationContrat : {}", id);
        return configurationContratRepository.findById(id).map(configurationContratMapper::toDto);
    }

    /**
     * Delete the configurationContrat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfigurationContrat : {}", id);
        configurationContratRepository.deleteById(id);
    }
}
