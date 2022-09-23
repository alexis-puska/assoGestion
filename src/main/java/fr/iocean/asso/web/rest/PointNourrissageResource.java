package fr.iocean.asso.web.rest;

import fr.iocean.asso.repository.PointNourrissageRepository;
import fr.iocean.asso.service.PointNourrissageService;
import fr.iocean.asso.service.dto.PointNourrissageDTO;
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
 * REST controller for managing {@link fr.iocean.asso.domain.PointNourrissage}.
 */
@RestController
@RequestMapping("/api")
public class PointNourrissageResource {

    private final Logger log = LoggerFactory.getLogger(PointNourrissageResource.class);

    private static final String ENTITY_NAME = "pointNourrissage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointNourrissageService pointNourrissageService;

    private final PointNourrissageRepository pointNourrissageRepository;

    public PointNourrissageResource(
        PointNourrissageService pointNourrissageService,
        PointNourrissageRepository pointNourrissageRepository
    ) {
        this.pointNourrissageService = pointNourrissageService;
        this.pointNourrissageRepository = pointNourrissageRepository;
    }

    /**
     * {@code POST  /point-nourrissages} : Create a new pointNourrissage.
     *
     * @param pointNourrissageDTO the pointNourrissageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointNourrissageDTO, or with status {@code 400 (Bad Request)} if the pointNourrissage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/point-nourrissages")
    public ResponseEntity<PointNourrissageDTO> createPointNourrissage(@RequestBody PointNourrissageDTO pointNourrissageDTO)
        throws URISyntaxException {
        log.debug("REST request to save PointNourrissage : {}", pointNourrissageDTO);
        if (pointNourrissageDTO.getId() != null) {
            throw new BadRequestAlertException("A new pointNourrissage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointNourrissageDTO result = pointNourrissageService.save(pointNourrissageDTO);
        return ResponseEntity
            .created(new URI("/api/point-nourrissages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-nourrissages/:id} : Updates an existing pointNourrissage.
     *
     * @param id the id of the pointNourrissageDTO to save.
     * @param pointNourrissageDTO the pointNourrissageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointNourrissageDTO,
     * or with status {@code 400 (Bad Request)} if the pointNourrissageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointNourrissageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/point-nourrissages/{id}")
    public ResponseEntity<PointNourrissageDTO> updatePointNourrissage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointNourrissageDTO pointNourrissageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PointNourrissage : {}, {}", id, pointNourrissageDTO);
        if (pointNourrissageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointNourrissageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointNourrissageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointNourrissageDTO result = pointNourrissageService.save(pointNourrissageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointNourrissageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /point-nourrissages/:id} : Partial updates given fields of an existing pointNourrissage, field will ignore if it is null
     *
     * @param id the id of the pointNourrissageDTO to save.
     * @param pointNourrissageDTO the pointNourrissageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointNourrissageDTO,
     * or with status {@code 400 (Bad Request)} if the pointNourrissageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pointNourrissageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointNourrissageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/point-nourrissages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointNourrissageDTO> partialUpdatePointNourrissage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointNourrissageDTO pointNourrissageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PointNourrissage partially : {}, {}", id, pointNourrissageDTO);
        if (pointNourrissageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointNourrissageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointNourrissageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointNourrissageDTO> result = pointNourrissageService.partialUpdate(pointNourrissageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointNourrissageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /point-nourrissages} : get all the pointNourrissages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointNourrissages in body.
     */
    @GetMapping("/point-nourrissages")
    public ResponseEntity<List<PointNourrissageDTO>> getAllPointNourrissages(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PointNourrissages");
        Page<PointNourrissageDTO> page = pointNourrissageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /point-nourrissages/:id} : get the "id" pointNourrissage.
     *
     * @param id the id of the pointNourrissageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointNourrissageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/point-nourrissages/{id}")
    public ResponseEntity<PointNourrissageDTO> getPointNourrissage(@PathVariable Long id) {
        log.debug("REST request to get PointNourrissage : {}", id);
        Optional<PointNourrissageDTO> pointNourrissageDTO = pointNourrissageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointNourrissageDTO);
    }

    /**
     * {@code DELETE  /point-nourrissages/:id} : delete the "id" pointNourrissage.
     *
     * @param id the id of the pointNourrissageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/point-nourrissages/{id}")
    public ResponseEntity<Void> deletePointNourrissage(@PathVariable Long id) {
        log.debug("REST request to delete PointNourrissage : {}", id);
        pointNourrissageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
