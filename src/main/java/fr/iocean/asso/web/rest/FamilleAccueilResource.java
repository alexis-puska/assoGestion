package fr.iocean.asso.web.rest;

import fr.iocean.asso.repository.FamilleAccueilRepository;
import fr.iocean.asso.service.FamilleAccueilService;
import fr.iocean.asso.service.dto.FamilleAccueilDTO;
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
 * REST controller for managing {@link fr.iocean.asso.domain.FamilleAccueil}.
 */
@RestController
@RequestMapping("/api")
public class FamilleAccueilResource {

    private final Logger log = LoggerFactory.getLogger(FamilleAccueilResource.class);

    private static final String ENTITY_NAME = "familleAccueil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilleAccueilService familleAccueilService;

    private final FamilleAccueilRepository familleAccueilRepository;

    public FamilleAccueilResource(FamilleAccueilService familleAccueilService, FamilleAccueilRepository familleAccueilRepository) {
        this.familleAccueilService = familleAccueilService;
        this.familleAccueilRepository = familleAccueilRepository;
    }

    /**
     * {@code POST  /famille-accueils} : Create a new familleAccueil.
     *
     * @param familleAccueilDTO the familleAccueilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familleAccueilDTO, or with status {@code 400 (Bad Request)} if the familleAccueil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/famille-accueils")
    public ResponseEntity<FamilleAccueilDTO> createFamilleAccueil(@RequestBody FamilleAccueilDTO familleAccueilDTO)
        throws URISyntaxException {
        log.debug("REST request to save FamilleAccueil : {}", familleAccueilDTO);
        if (familleAccueilDTO.getId() != null) {
            throw new BadRequestAlertException("A new familleAccueil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilleAccueilDTO result = familleAccueilService.save(familleAccueilDTO);
        return ResponseEntity
            .created(new URI("/api/famille-accueils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /famille-accueils/:id} : Updates an existing familleAccueil.
     *
     * @param id the id of the familleAccueilDTO to save.
     * @param familleAccueilDTO the familleAccueilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familleAccueilDTO,
     * or with status {@code 400 (Bad Request)} if the familleAccueilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familleAccueilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/famille-accueils/{id}")
    public ResponseEntity<FamilleAccueilDTO> updateFamilleAccueil(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FamilleAccueilDTO familleAccueilDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FamilleAccueil : {}, {}", id, familleAccueilDTO);
        if (familleAccueilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familleAccueilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familleAccueilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FamilleAccueilDTO result = familleAccueilService.save(familleAccueilDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familleAccueilDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /famille-accueils/:id} : Partial updates given fields of an existing familleAccueil, field will ignore if it is null
     *
     * @param id the id of the familleAccueilDTO to save.
     * @param familleAccueilDTO the familleAccueilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familleAccueilDTO,
     * or with status {@code 400 (Bad Request)} if the familleAccueilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the familleAccueilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the familleAccueilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/famille-accueils/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FamilleAccueilDTO> partialUpdateFamilleAccueil(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FamilleAccueilDTO familleAccueilDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FamilleAccueil partially : {}, {}", id, familleAccueilDTO);
        if (familleAccueilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familleAccueilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familleAccueilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilleAccueilDTO> result = familleAccueilService.partialUpdate(familleAccueilDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familleAccueilDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /famille-accueils} : get all the familleAccueils.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familleAccueils in body.
     */
    @GetMapping("/famille-accueils")
    public ResponseEntity<List<FamilleAccueilDTO>> getAllFamilleAccueils(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FamilleAccueils");
        Page<FamilleAccueilDTO> page = familleAccueilService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /famille-accueils/:id} : get the "id" familleAccueil.
     *
     * @param id the id of the familleAccueilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familleAccueilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/famille-accueils/{id}")
    public ResponseEntity<FamilleAccueilDTO> getFamilleAccueil(@PathVariable Long id) {
        log.debug("REST request to get FamilleAccueil : {}", id);
        Optional<FamilleAccueilDTO> familleAccueilDTO = familleAccueilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familleAccueilDTO);
    }

    /**
     * {@code DELETE  /famille-accueils/:id} : delete the "id" familleAccueil.
     *
     * @param id the id of the familleAccueilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/famille-accueils/{id}")
    public ResponseEntity<Void> deleteFamilleAccueil(@PathVariable Long id) {
        log.debug("REST request to delete FamilleAccueil : {}", id);
        familleAccueilService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
