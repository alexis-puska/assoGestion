package fr.iocean.asso.web.rest;

import fr.iocean.asso.repository.ConfigurationContratRepository;
import fr.iocean.asso.service.ConfigurationContratService;
import fr.iocean.asso.service.dto.ConfigurationContratDTO;
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
 * REST controller for managing {@link fr.iocean.asso.domain.ConfigurationContrat}.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationContratResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationContratResource.class);

    private static final String ENTITY_NAME = "configurationContrat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationContratService configurationContratService;

    private final ConfigurationContratRepository configurationContratRepository;

    public ConfigurationContratResource(
        ConfigurationContratService configurationContratService,
        ConfigurationContratRepository configurationContratRepository
    ) {
        this.configurationContratService = configurationContratService;
        this.configurationContratRepository = configurationContratRepository;
    }

    /**
     * {@code POST  /configuration-contrats} : Create a new configurationContrat.
     *
     * @param configurationContratDTO the configurationContratDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configurationContratDTO, or with status {@code 400 (Bad Request)} if the configurationContrat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configuration-contrats")
    public ResponseEntity<ConfigurationContratDTO> createConfigurationContrat(@RequestBody ConfigurationContratDTO configurationContratDTO)
        throws URISyntaxException {
        log.debug("REST request to save ConfigurationContrat : {}", configurationContratDTO);
        if (configurationContratDTO.getId() != null) {
            throw new BadRequestAlertException("A new configurationContrat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigurationContratDTO result = configurationContratService.save(configurationContratDTO);
        return ResponseEntity
            .created(new URI("/api/configuration-contrats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configuration-contrats/:id} : Updates an existing configurationContrat.
     *
     * @param id the id of the configurationContratDTO to save.
     * @param configurationContratDTO the configurationContratDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurationContratDTO,
     * or with status {@code 400 (Bad Request)} if the configurationContratDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configurationContratDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configuration-contrats/{id}")
    public ResponseEntity<ConfigurationContratDTO> updateConfigurationContrat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConfigurationContratDTO configurationContratDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ConfigurationContrat : {}, {}", id, configurationContratDTO);
        if (configurationContratDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configurationContratDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configurationContratRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConfigurationContratDTO result = configurationContratService.save(configurationContratDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationContratDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /configuration-contrats/:id} : Partial updates given fields of an existing configurationContrat, field will ignore if it is null
     *
     * @param id the id of the configurationContratDTO to save.
     * @param configurationContratDTO the configurationContratDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurationContratDTO,
     * or with status {@code 400 (Bad Request)} if the configurationContratDTO is not valid,
     * or with status {@code 404 (Not Found)} if the configurationContratDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the configurationContratDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/configuration-contrats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConfigurationContratDTO> partialUpdateConfigurationContrat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConfigurationContratDTO configurationContratDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConfigurationContrat partially : {}, {}", id, configurationContratDTO);
        if (configurationContratDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configurationContratDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configurationContratRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConfigurationContratDTO> result = configurationContratService.partialUpdate(configurationContratDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationContratDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /configuration-contrats} : get all the configurationContrats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configurationContrats in body.
     */
    @GetMapping("/configuration-contrats")
    public ResponseEntity<List<ConfigurationContratDTO>> getAllConfigurationContrats(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ConfigurationContrats");
        Page<ConfigurationContratDTO> page = configurationContratService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configuration-contrats/:id} : get the "id" configurationContrat.
     *
     * @param id the id of the configurationContratDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configurationContratDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configuration-contrats/{id}")
    public ResponseEntity<ConfigurationContratDTO> getConfigurationContrat(@PathVariable Long id) {
        log.debug("REST request to get ConfigurationContrat : {}", id);
        Optional<ConfigurationContratDTO> configurationContratDTO = configurationContratService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configurationContratDTO);
    }

    /**
     * {@code DELETE  /configuration-contrats/:id} : delete the "id" configurationContrat.
     *
     * @param id the id of the configurationContratDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configuration-contrats/{id}")
    public ResponseEntity<Void> deleteConfigurationContrat(@PathVariable Long id) {
        log.debug("REST request to delete ConfigurationContrat : {}", id);
        configurationContratService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
