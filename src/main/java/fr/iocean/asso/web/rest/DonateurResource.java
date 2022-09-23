package fr.iocean.asso.web.rest;

import fr.iocean.asso.repository.DonateurRepository;
import fr.iocean.asso.service.DonateurService;
import fr.iocean.asso.service.dto.DonateurDTO;
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
 * REST controller for managing {@link fr.iocean.asso.domain.Donateur}.
 */
@RestController
@RequestMapping("/api")
public class DonateurResource {

    private final Logger log = LoggerFactory.getLogger(DonateurResource.class);

    private static final String ENTITY_NAME = "donateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonateurService donateurService;

    private final DonateurRepository donateurRepository;

    public DonateurResource(DonateurService donateurService, DonateurRepository donateurRepository) {
        this.donateurService = donateurService;
        this.donateurRepository = donateurRepository;
    }

    /**
     * {@code POST  /donateurs} : Create a new donateur.
     *
     * @param donateurDTO the donateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donateurDTO, or with status {@code 400 (Bad Request)} if the donateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donateurs")
    public ResponseEntity<DonateurDTO> createDonateur(@RequestBody DonateurDTO donateurDTO) throws URISyntaxException {
        log.debug("REST request to save Donateur : {}", donateurDTO);
        if (donateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new donateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DonateurDTO result = donateurService.save(donateurDTO);
        return ResponseEntity
            .created(new URI("/api/donateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /donateurs/:id} : Updates an existing donateur.
     *
     * @param id the id of the donateurDTO to save.
     * @param donateurDTO the donateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donateurDTO,
     * or with status {@code 400 (Bad Request)} if the donateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donateurs/{id}")
    public ResponseEntity<DonateurDTO> updateDonateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DonateurDTO donateurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Donateur : {}, {}", id, donateurDTO);
        if (donateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DonateurDTO result = donateurService.save(donateurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donateurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /donateurs/:id} : Partial updates given fields of an existing donateur, field will ignore if it is null
     *
     * @param id the id of the donateurDTO to save.
     * @param donateurDTO the donateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donateurDTO,
     * or with status {@code 400 (Bad Request)} if the donateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the donateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the donateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/donateurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DonateurDTO> partialUpdateDonateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DonateurDTO donateurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Donateur partially : {}, {}", id, donateurDTO);
        if (donateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DonateurDTO> result = donateurService.partialUpdate(donateurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /donateurs} : get all the donateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donateurs in body.
     */
    @GetMapping("/donateurs")
    public ResponseEntity<List<DonateurDTO>> getAllDonateurs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Donateurs");
        Page<DonateurDTO> page = donateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /donateurs/:id} : get the "id" donateur.
     *
     * @param id the id of the donateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/donateurs/{id}")
    public ResponseEntity<DonateurDTO> getDonateur(@PathVariable Long id) {
        log.debug("REST request to get Donateur : {}", id);
        Optional<DonateurDTO> donateurDTO = donateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(donateurDTO);
    }

    /**
     * {@code DELETE  /donateurs/:id} : delete the "id" donateur.
     *
     * @param id the id of the donateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/donateurs/{id}")
    public ResponseEntity<Void> deleteDonateur(@PathVariable Long id) {
        log.debug("REST request to delete Donateur : {}", id);
        donateurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
