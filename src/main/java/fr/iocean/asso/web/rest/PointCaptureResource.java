package fr.iocean.asso.web.rest;

import fr.iocean.asso.repository.PointCaptureRepository;
import fr.iocean.asso.service.PointCaptureService;
import fr.iocean.asso.service.dto.PointCaptureDTO;
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
 * REST controller for managing {@link fr.iocean.asso.domain.PointCapture}.
 */
@RestController
@RequestMapping("/api")
public class PointCaptureResource {

    private final Logger log = LoggerFactory.getLogger(PointCaptureResource.class);

    private static final String ENTITY_NAME = "pointCapture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointCaptureService pointCaptureService;

    private final PointCaptureRepository pointCaptureRepository;

    public PointCaptureResource(PointCaptureService pointCaptureService, PointCaptureRepository pointCaptureRepository) {
        this.pointCaptureService = pointCaptureService;
        this.pointCaptureRepository = pointCaptureRepository;
    }

    /**
     * {@code POST  /point-captures} : Create a new pointCapture.
     *
     * @param pointCaptureDTO the pointCaptureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointCaptureDTO, or with status {@code 400 (Bad Request)} if the pointCapture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/point-captures")
    public ResponseEntity<PointCaptureDTO> createPointCapture(@RequestBody PointCaptureDTO pointCaptureDTO) throws URISyntaxException {
        log.debug("REST request to save PointCapture : {}", pointCaptureDTO);
        if (pointCaptureDTO.getId() != null) {
            throw new BadRequestAlertException("A new pointCapture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointCaptureDTO result = pointCaptureService.save(pointCaptureDTO);
        return ResponseEntity
            .created(new URI("/api/point-captures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-captures/:id} : Updates an existing pointCapture.
     *
     * @param id the id of the pointCaptureDTO to save.
     * @param pointCaptureDTO the pointCaptureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointCaptureDTO,
     * or with status {@code 400 (Bad Request)} if the pointCaptureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointCaptureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/point-captures/{id}")
    public ResponseEntity<PointCaptureDTO> updatePointCapture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointCaptureDTO pointCaptureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PointCapture : {}, {}", id, pointCaptureDTO);
        if (pointCaptureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointCaptureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointCaptureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointCaptureDTO result = pointCaptureService.save(pointCaptureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointCaptureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /point-captures/:id} : Partial updates given fields of an existing pointCapture, field will ignore if it is null
     *
     * @param id the id of the pointCaptureDTO to save.
     * @param pointCaptureDTO the pointCaptureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointCaptureDTO,
     * or with status {@code 400 (Bad Request)} if the pointCaptureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pointCaptureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointCaptureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/point-captures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointCaptureDTO> partialUpdatePointCapture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointCaptureDTO pointCaptureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PointCapture partially : {}, {}", id, pointCaptureDTO);
        if (pointCaptureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointCaptureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointCaptureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointCaptureDTO> result = pointCaptureService.partialUpdate(pointCaptureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointCaptureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /point-captures} : get all the pointCaptures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointCaptures in body.
     */
    @GetMapping("/point-captures")
    public ResponseEntity<List<PointCaptureDTO>> getAllPointCaptures(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PointCaptures");
        Page<PointCaptureDTO> page = pointCaptureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /point-captures/:id} : get the "id" pointCapture.
     *
     * @param id the id of the pointCaptureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointCaptureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/point-captures/{id}")
    public ResponseEntity<PointCaptureDTO> getPointCapture(@PathVariable Long id) {
        log.debug("REST request to get PointCapture : {}", id);
        Optional<PointCaptureDTO> pointCaptureDTO = pointCaptureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointCaptureDTO);
    }

    /**
     * {@code DELETE  /point-captures/:id} : delete the "id" pointCapture.
     *
     * @param id the id of the pointCaptureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/point-captures/{id}")
    public ResponseEntity<Void> deletePointCapture(@PathVariable Long id) {
        log.debug("REST request to delete PointCapture : {}", id);
        pointCaptureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
