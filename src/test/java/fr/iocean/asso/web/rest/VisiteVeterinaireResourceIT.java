package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.VisiteVeterinaire;
import fr.iocean.asso.repository.VisiteVeterinaireRepository;
import fr.iocean.asso.service.dto.VisiteVeterinaireDTO;
import fr.iocean.asso.service.mapper.VisiteVeterinaireMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VisiteVeterinaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VisiteVeterinaireResourceIT {

    private static final LocalDate DEFAULT_DATE_VISITE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VISITE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/visite-veterinaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VisiteVeterinaireRepository visiteVeterinaireRepository;

    @Autowired
    private VisiteVeterinaireMapper visiteVeterinaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisiteVeterinaireMockMvc;

    private VisiteVeterinaire visiteVeterinaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VisiteVeterinaire createEntity(EntityManager em) {
        VisiteVeterinaire visiteVeterinaire = new VisiteVeterinaire().dateVisite(DEFAULT_DATE_VISITE);
        return visiteVeterinaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VisiteVeterinaire createUpdatedEntity(EntityManager em) {
        VisiteVeterinaire visiteVeterinaire = new VisiteVeterinaire().dateVisite(UPDATED_DATE_VISITE);
        return visiteVeterinaire;
    }

    @BeforeEach
    public void initTest() {
        visiteVeterinaire = createEntity(em);
    }

    @Test
    @Transactional
    void createVisiteVeterinaire() throws Exception {
        int databaseSizeBeforeCreate = visiteVeterinaireRepository.findAll().size();
        // Create the VisiteVeterinaire
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(visiteVeterinaire);
        restVisiteVeterinaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeCreate + 1);
        VisiteVeterinaire testVisiteVeterinaire = visiteVeterinaireList.get(visiteVeterinaireList.size() - 1);
        assertThat(testVisiteVeterinaire.getDateVisite()).isEqualTo(DEFAULT_DATE_VISITE);
    }

    @Test
    @Transactional
    void createVisiteVeterinaireWithExistingId() throws Exception {
        // Create the VisiteVeterinaire with an existing ID
        visiteVeterinaire.setId(1L);
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(visiteVeterinaire);

        int databaseSizeBeforeCreate = visiteVeterinaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisiteVeterinaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVisiteVeterinaires() throws Exception {
        // Initialize the database
        visiteVeterinaireRepository.saveAndFlush(visiteVeterinaire);

        // Get all the visiteVeterinaireList
        restVisiteVeterinaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visiteVeterinaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateVisite").value(hasItem(DEFAULT_DATE_VISITE.toString())));
    }

    @Test
    @Transactional
    void getVisiteVeterinaire() throws Exception {
        // Initialize the database
        visiteVeterinaireRepository.saveAndFlush(visiteVeterinaire);

        // Get the visiteVeterinaire
        restVisiteVeterinaireMockMvc
            .perform(get(ENTITY_API_URL_ID, visiteVeterinaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visiteVeterinaire.getId().intValue()))
            .andExpect(jsonPath("$.dateVisite").value(DEFAULT_DATE_VISITE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVisiteVeterinaire() throws Exception {
        // Get the visiteVeterinaire
        restVisiteVeterinaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVisiteVeterinaire() throws Exception {
        // Initialize the database
        visiteVeterinaireRepository.saveAndFlush(visiteVeterinaire);

        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();

        // Update the visiteVeterinaire
        VisiteVeterinaire updatedVisiteVeterinaire = visiteVeterinaireRepository.findById(visiteVeterinaire.getId()).get();
        // Disconnect from session so that the updates on updatedVisiteVeterinaire are not directly saved in db
        em.detach(updatedVisiteVeterinaire);
        updatedVisiteVeterinaire.dateVisite(UPDATED_DATE_VISITE);
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(updatedVisiteVeterinaire);

        restVisiteVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visiteVeterinaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        VisiteVeterinaire testVisiteVeterinaire = visiteVeterinaireList.get(visiteVeterinaireList.size() - 1);
        assertThat(testVisiteVeterinaire.getDateVisite()).isEqualTo(UPDATED_DATE_VISITE);
    }

    @Test
    @Transactional
    void putNonExistingVisiteVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();
        visiteVeterinaire.setId(count.incrementAndGet());

        // Create the VisiteVeterinaire
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(visiteVeterinaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisiteVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visiteVeterinaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisiteVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();
        visiteVeterinaire.setId(count.incrementAndGet());

        // Create the VisiteVeterinaire
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(visiteVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisiteVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisiteVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();
        visiteVeterinaire.setId(count.incrementAndGet());

        // Create the VisiteVeterinaire
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(visiteVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisiteVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVisiteVeterinaireWithPatch() throws Exception {
        // Initialize the database
        visiteVeterinaireRepository.saveAndFlush(visiteVeterinaire);

        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();

        // Update the visiteVeterinaire using partial update
        VisiteVeterinaire partialUpdatedVisiteVeterinaire = new VisiteVeterinaire();
        partialUpdatedVisiteVeterinaire.setId(visiteVeterinaire.getId());

        restVisiteVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisiteVeterinaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisiteVeterinaire))
            )
            .andExpect(status().isOk());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        VisiteVeterinaire testVisiteVeterinaire = visiteVeterinaireList.get(visiteVeterinaireList.size() - 1);
        assertThat(testVisiteVeterinaire.getDateVisite()).isEqualTo(DEFAULT_DATE_VISITE);
    }

    @Test
    @Transactional
    void fullUpdateVisiteVeterinaireWithPatch() throws Exception {
        // Initialize the database
        visiteVeterinaireRepository.saveAndFlush(visiteVeterinaire);

        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();

        // Update the visiteVeterinaire using partial update
        VisiteVeterinaire partialUpdatedVisiteVeterinaire = new VisiteVeterinaire();
        partialUpdatedVisiteVeterinaire.setId(visiteVeterinaire.getId());

        partialUpdatedVisiteVeterinaire.dateVisite(UPDATED_DATE_VISITE);

        restVisiteVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisiteVeterinaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisiteVeterinaire))
            )
            .andExpect(status().isOk());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        VisiteVeterinaire testVisiteVeterinaire = visiteVeterinaireList.get(visiteVeterinaireList.size() - 1);
        assertThat(testVisiteVeterinaire.getDateVisite()).isEqualTo(UPDATED_DATE_VISITE);
    }

    @Test
    @Transactional
    void patchNonExistingVisiteVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();
        visiteVeterinaire.setId(count.incrementAndGet());

        // Create the VisiteVeterinaire
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(visiteVeterinaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisiteVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visiteVeterinaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisiteVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();
        visiteVeterinaire.setId(count.incrementAndGet());

        // Create the VisiteVeterinaire
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(visiteVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisiteVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisiteVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = visiteVeterinaireRepository.findAll().size();
        visiteVeterinaire.setId(count.incrementAndGet());

        // Create the VisiteVeterinaire
        VisiteVeterinaireDTO visiteVeterinaireDTO = visiteVeterinaireMapper.toDto(visiteVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisiteVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visiteVeterinaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VisiteVeterinaire in the database
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVisiteVeterinaire() throws Exception {
        // Initialize the database
        visiteVeterinaireRepository.saveAndFlush(visiteVeterinaire);

        int databaseSizeBeforeDelete = visiteVeterinaireRepository.findAll().size();

        // Delete the visiteVeterinaire
        restVisiteVeterinaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, visiteVeterinaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VisiteVeterinaire> visiteVeterinaireList = visiteVeterinaireRepository.findAll();
        assertThat(visiteVeterinaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
