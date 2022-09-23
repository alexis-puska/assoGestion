package fr.iocean.asso.web.rest;

import fr.iocean.asso.repository.RaceChatRepository;
import fr.iocean.asso.service.RaceChatService;
import fr.iocean.asso.service.dto.RaceChatDTO;
import fr.iocean.asso.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.iocean.asso.domain.RaceChat}.
 */
@RestController
@RequestMapping("/api")
public class RaceChatResource {

    private final Logger log = LoggerFactory.getLogger(RaceChatResource.class);

    private static final String ENTITY_NAME = "raceChat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaceChatService raceChatService;

    private final RaceChatRepository raceChatRepository;

    public RaceChatResource(RaceChatService raceChatService, RaceChatRepository raceChatRepository) {
        this.raceChatService = raceChatService;
        this.raceChatRepository = raceChatRepository;
    }

    /**
     * {@code POST  /race-chats} : Create a new raceChat.
     *
     * @param raceChatDTO the raceChatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raceChatDTO, or with status {@code 400 (Bad Request)} if the raceChat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/race-chats")
    public ResponseEntity<RaceChatDTO> createRaceChat(@RequestBody RaceChatDTO raceChatDTO) throws URISyntaxException {
        log.debug("REST request to save RaceChat : {}", raceChatDTO);
        if (raceChatDTO.getId() != null) {
            throw new BadRequestAlertException("A new raceChat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RaceChatDTO result = raceChatService.save(raceChatDTO);
        return ResponseEntity
            .created(new URI("/api/race-chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /race-chats/:id} : Updates an existing raceChat.
     *
     * @param id the id of the raceChatDTO to save.
     * @param raceChatDTO the raceChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raceChatDTO,
     * or with status {@code 400 (Bad Request)} if the raceChatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raceChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/race-chats/{id}")
    public ResponseEntity<RaceChatDTO> updateRaceChat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RaceChatDTO raceChatDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RaceChat : {}, {}", id, raceChatDTO);
        if (raceChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raceChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!raceChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RaceChatDTO result = raceChatService.save(raceChatDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, raceChatDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /race-chats/:id} : Partial updates given fields of an existing raceChat, field will ignore if it is null
     *
     * @param id the id of the raceChatDTO to save.
     * @param raceChatDTO the raceChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raceChatDTO,
     * or with status {@code 400 (Bad Request)} if the raceChatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the raceChatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the raceChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/race-chats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RaceChatDTO> partialUpdateRaceChat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RaceChatDTO raceChatDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RaceChat partially : {}, {}", id, raceChatDTO);
        if (raceChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raceChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!raceChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RaceChatDTO> result = raceChatService.partialUpdate(raceChatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, raceChatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /race-chats} : get all the raceChats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raceChats in body.
     */
    @GetMapping("/race-chats")
    public ResponseEntity<List<RaceChatDTO>> getAllRaceChats(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RaceChats");
        Page<RaceChatDTO> page = raceChatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /race-chats/:id} : get the "id" raceChat.
     *
     * @param id the id of the raceChatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raceChatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/race-chats/{id}")
    public ResponseEntity<RaceChatDTO> getRaceChat(@PathVariable Long id) {
        log.debug("REST request to get RaceChat : {}", id);
        Optional<RaceChatDTO> raceChatDTO = raceChatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raceChatDTO);
    }

    /**
     * {@code DELETE  /race-chats/:id} : delete the "id" raceChat.
     *
     * @param id the id of the raceChatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/race-chats/{id}")
    public ResponseEntity<Void> deleteRaceChat(@PathVariable Long id) {
        log.debug("REST request to delete RaceChat : {}", id);
        raceChatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
