package fr.iocean.asso.web.rest;

import fr.iocean.asso.repository.ConfigurationDonRepository;
import fr.iocean.asso.service.ConfigurationDonService;
import fr.iocean.asso.service.dto.ConfigurationDonDTO;
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
 * REST controller for managing {@link fr.iocean.asso.domain.ConfigurationDon}.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationDonResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationDonResource.class);

    private static final String ENTITY_NAME = "configurationDon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationDonService configurationDonService;

    private final ConfigurationDonRepository configurationDonRepository;

    public ConfigurationDonResource(
        ConfigurationDonService configurationDonService,
        ConfigurationDonRepository configurationDonRepository
    ) {
        this.configurationDonService = configurationDonService;
        this.configurationDonRepository = configurationDonRepository;
    }

    /**
     * {@code POST  /configuration-dons} : Create a new configurationDon.
     *
     * @param configurationDonDTO the configurationDonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configurationDonDTO, or with status {@code 400 (Bad Request)} if the configurationDon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configuration-dons")
    public ResponseEntity<ConfigurationDonDTO> createConfigurationDon(@RequestBody ConfigurationDonDTO configurationDonDTO)
        throws URISyntaxException {
        log.debug("REST request to save ConfigurationDon : {}", configurationDonDTO);
        if (configurationDonDTO.getId() != null) {
            throw new BadRequestAlertException("A new configurationDon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigurationDonDTO result = configurationDonService.save(configurationDonDTO);
        return ResponseEntity
            .created(new URI("/api/configuration-dons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configuration-dons/:id} : Updates an existing configurationDon.
     *
     * @param id the id of the configurationDonDTO to save.
     * @param configurationDonDTO the configurationDonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurationDonDTO,
     * or with status {@code 400 (Bad Request)} if the configurationDonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configurationDonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configuration-dons/{id}")
    public ResponseEntity<ConfigurationDonDTO> updateConfigurationDon(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConfigurationDonDTO configurationDonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ConfigurationDon : {}, {}", id, configurationDonDTO);
        if (configurationDonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configurationDonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configurationDonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConfigurationDonDTO result = configurationDonService.save(configurationDonDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationDonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /configuration-dons/:id} : Partial updates given fields of an existing configurationDon, field will ignore if it is null
     *
     * @param id the id of the configurationDonDTO to save.
     * @param configurationDonDTO the configurationDonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurationDonDTO,
     * or with status {@code 400 (Bad Request)} if the configurationDonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the configurationDonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the configurationDonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/configuration-dons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConfigurationDonDTO> partialUpdateConfigurationDon(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConfigurationDonDTO configurationDonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConfigurationDon partially : {}, {}", id, configurationDonDTO);
        if (configurationDonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configurationDonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configurationDonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConfigurationDonDTO> result = configurationDonService.partialUpdate(configurationDonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationDonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /configuration-dons} : get all the configurationDons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configurationDons in body.
     */
    @GetMapping("/configuration-dons")
    public ResponseEntity<List<ConfigurationDonDTO>> getAllConfigurationDons(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ConfigurationDons");
        Page<ConfigurationDonDTO> page = configurationDonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configuration-dons/:id} : get the "id" configurationDon.
     *
     * @param id the id of the configurationDonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configurationDonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configuration-dons/{id}")
    public ResponseEntity<ConfigurationDonDTO> getConfigurationDon(@PathVariable Long id) {
        log.debug("REST request to get ConfigurationDon : {}", id);
        Optional<ConfigurationDonDTO> configurationDonDTO = configurationDonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configurationDonDTO);
    }

    /**
     * {@code DELETE  /configuration-dons/:id} : delete the "id" configurationDon.
     *
     * @param id the id of the configurationDonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configuration-dons/{id}")
    public ResponseEntity<Void> deleteConfigurationDon(@PathVariable Long id) {
        log.debug("REST request to delete ConfigurationDon : {}", id);
        configurationDonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
