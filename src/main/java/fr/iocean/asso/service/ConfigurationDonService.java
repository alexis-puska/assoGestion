package fr.iocean.asso.service;

import fr.iocean.asso.domain.ConfigurationDon;
import fr.iocean.asso.repository.ConfigurationDonRepository;
import fr.iocean.asso.service.dto.ConfigurationDonDTO;
import fr.iocean.asso.service.mapper.ConfigurationDonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConfigurationDon}.
 */
@Service
@Transactional
public class ConfigurationDonService {

    private final Logger log = LoggerFactory.getLogger(ConfigurationDonService.class);

    private final ConfigurationDonRepository configurationDonRepository;

    private final ConfigurationDonMapper configurationDonMapper;

    public ConfigurationDonService(ConfigurationDonRepository configurationDonRepository, ConfigurationDonMapper configurationDonMapper) {
        this.configurationDonRepository = configurationDonRepository;
        this.configurationDonMapper = configurationDonMapper;
    }

    /**
     * Save a configurationDon.
     *
     * @param configurationDonDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfigurationDonDTO save(ConfigurationDonDTO configurationDonDTO) {
        log.debug("Request to save ConfigurationDon : {}", configurationDonDTO);
        ConfigurationDon configurationDon = configurationDonMapper.toEntity(configurationDonDTO);
        configurationDon = configurationDonRepository.save(configurationDon);
        return configurationDonMapper.toDto(configurationDon);
    }

    /**
     * Partially update a configurationDon.
     *
     * @param configurationDonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConfigurationDonDTO> partialUpdate(ConfigurationDonDTO configurationDonDTO) {
        log.debug("Request to partially update ConfigurationDon : {}", configurationDonDTO);

        return configurationDonRepository
            .findById(configurationDonDTO.getId())
            .map(existingConfigurationDon -> {
                configurationDonMapper.partialUpdate(existingConfigurationDon, configurationDonDTO);

                return existingConfigurationDon;
            })
            .map(configurationDonRepository::save)
            .map(configurationDonMapper::toDto);
    }

    /**
     * Get all the configurationDons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationDonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigurationDons");
        return configurationDonRepository.findAll(pageable).map(configurationDonMapper::toDto);
    }

    /**
     * Get one configurationDon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfigurationDonDTO> findOne(Long id) {
        log.debug("Request to get ConfigurationDon : {}", id);
        return configurationDonRepository.findById(id).map(configurationDonMapper::toDto);
    }

    /**
     * Delete the configurationDon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfigurationDon : {}", id);
        configurationDonRepository.deleteById(id);
    }
}
