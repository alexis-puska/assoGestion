package fr.iocean.asso.web.rest;

import fr.iocean.asso.repository.ActeVeterinaireRepository;
import fr.iocean.asso.service.ActeVeterinaireService;
import fr.iocean.asso.service.dto.ActeVeterinaireDTO;
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
 * REST controller for managing {@link fr.iocean.asso.domain.ActeVeterinaire}.
 */
@RestController
@RequestMapping("/api")
public class ActeVeterinaireResource {

    private final Logger log = LoggerFactory.getLogger(ActeVeterinaireResource.class);

    private static final String ENTITY_NAME = "acteVeterinaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActeVeterinaireService acteVeterinaireService;

    private final ActeVeterinaireRepository acteVeterinaireRepository;

    public ActeVeterinaireResource(ActeVeterinaireService acteVeterinaireService, ActeVeterinaireRepository acteVeterinaireRepository) {
        this.acteVeterinaireService = acteVeterinaireService;
        this.acteVeterinaireRepository = acteVeterinaireRepository;
    }

    /**
     * {@code POST  /acte-veterinaires} : Create a new acteVeterinaire.
     *
     * @param acteVeterinaireDTO the acteVeterinaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acteVeterinaireDTO, or with status {@code 400 (Bad Request)} if the acteVeterinaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acte-veterinaires")
    public ResponseEntity<ActeVeterinaireDTO> createActeVeterinaire(@RequestBody ActeVeterinaireDTO acteVeterinaireDTO)
        throws URISyntaxException {
        log.debug("REST request to save ActeVeterinaire : {}", acteVeterinaireDTO);
        if (acteVeterinaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new acteVeterinaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActeVeterinaireDTO result = acteVeterinaireService.save(acteVeterinaireDTO);
        return ResponseEntity
            .created(new URI("/api/acte-veterinaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acte-veterinaires/:id} : Updates an existing acteVeterinaire.
     *
     * @param id the id of the acteVeterinaireDTO to save.
     * @param acteVeterinaireDTO the acteVeterinaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acteVeterinaireDTO,
     * or with status {@code 400 (Bad Request)} if the acteVeterinaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acteVeterinaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acte-veterinaires/{id}")
    public ResponseEntity<ActeVeterinaireDTO> updateActeVeterinaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActeVeterinaireDTO acteVeterinaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ActeVeterinaire : {}, {}", id, acteVeterinaireDTO);
        if (acteVeterinaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acteVeterinaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acteVeterinaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ActeVeterinaireDTO result = acteVeterinaireService.save(acteVeterinaireDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acteVeterinaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acte-veterinaires/:id} : Partial updates given fields of an existing acteVeterinaire, field will ignore if it is null
     *
     * @param id the id of the acteVeterinaireDTO to save.
     * @param acteVeterinaireDTO the acteVeterinaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acteVeterinaireDTO,
     * or with status {@code 400 (Bad Request)} if the acteVeterinaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the acteVeterinaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the acteVeterinaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acte-veterinaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActeVeterinaireDTO> partialUpdateActeVeterinaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActeVeterinaireDTO acteVeterinaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActeVeterinaire partially : {}, {}", id, acteVeterinaireDTO);
        if (acteVeterinaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acteVeterinaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acteVeterinaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActeVeterinaireDTO> result = acteVeterinaireService.partialUpdate(acteVeterinaireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acteVeterinaireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acte-veterinaires} : get all the acteVeterinaires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acteVeterinaires in body.
     */
    @GetMapping("/acte-veterinaires")
    public ResponseEntity<List<ActeVeterinaireDTO>> getAllActeVeterinaires(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ActeVeterinaires");
        Page<ActeVeterinaireDTO> page = acteVeterinaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acte-veterinaires/:id} : get the "id" acteVeterinaire.
     *
     * @param id the id of the acteVeterinaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acteVeterinaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acte-veterinaires/{id}")
    public ResponseEntity<ActeVeterinaireDTO> getActeVeterinaire(@PathVariable Long id) {
        log.debug("REST request to get ActeVeterinaire : {}", id);
        Optional<ActeVeterinaireDTO> acteVeterinaireDTO = acteVeterinaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acteVeterinaireDTO);
    }

    /**
     * {@code DELETE  /acte-veterinaires/:id} : delete the "id" acteVeterinaire.
     *
     * @param id the id of the acteVeterinaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acte-veterinaires/{id}")
    public ResponseEntity<Void> deleteActeVeterinaire(@PathVariable Long id) {
        log.debug("REST request to delete ActeVeterinaire : {}", id);
        acteVeterinaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
