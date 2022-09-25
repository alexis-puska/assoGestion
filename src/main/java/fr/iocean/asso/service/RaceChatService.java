package fr.iocean.asso.service;

import fr.iocean.asso.domain.RaceChat;
import fr.iocean.asso.repository.RaceChatRepository;
import fr.iocean.asso.service.dto.RaceChatDTO;
import fr.iocean.asso.service.mapper.RaceChatMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RaceChat}.
 */
@Service
@Transactional
public class RaceChatService {

    private final Logger log = LoggerFactory.getLogger(RaceChatService.class);

    private final RaceChatRepository raceChatRepository;

    private final RaceChatMapper raceChatMapper;

    public RaceChatService(RaceChatRepository raceChatRepository, RaceChatMapper raceChatMapper) {
        this.raceChatRepository = raceChatRepository;
        this.raceChatMapper = raceChatMapper;
    }

    /**
     * Save a raceChat.
     *
     * @param raceChatDTO the entity to save.
     * @return the persisted entity.
     */
    public RaceChatDTO save(RaceChatDTO raceChatDTO) {
        log.debug("Request to save RaceChat : {}", raceChatDTO);
        RaceChat raceChat = raceChatMapper.toEntity(raceChatDTO);
        raceChat = raceChatRepository.save(raceChat);
        return raceChatMapper.toDto(raceChat);
    }

    /**
     * Partially update a raceChat.
     *
     * @param raceChatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RaceChatDTO> partialUpdate(RaceChatDTO raceChatDTO) {
        log.debug("Request to partially update RaceChat : {}", raceChatDTO);

        return raceChatRepository
            .findById(raceChatDTO.getId())
            .map(existingRaceChat -> {
                raceChatMapper.partialUpdate(existingRaceChat, raceChatDTO);

                return existingRaceChat;
            })
            .map(raceChatRepository::save)
            .map(raceChatMapper::toDto);
    }

    /**
     * Get all the raceChats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceChatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RaceChats");
        return raceChatRepository.findAll(pageable).map(raceChatMapper::toDto);
    }

    /**
     * Get one raceChat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RaceChatDTO> findOne(Long id) {
        log.debug("Request to get RaceChat : {}", id);
        return raceChatRepository.findById(id).map(raceChatMapper::toDto);
    }

    /**
     * Delete the raceChat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RaceChat : {}", id);
        raceChatRepository.deleteById(id);
    }

    public List<RaceChatDTO> findAutocomplete(String query) {
        return this.raceChatMapper.toDto(this.raceChatRepository.findAutocomplete("%" + query + "%"));
    }
}
