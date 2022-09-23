package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.PointNourrissage;
import fr.iocean.asso.repository.PointNourrissageRepository;
import fr.iocean.asso.service.dto.PointNourrissageDTO;
import fr.iocean.asso.service.mapper.PointNourrissageMapper;
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
 * Integration tests for the {@link PointNourrissageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointNourrissageResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/point-nourrissages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointNourrissageRepository pointNourrissageRepository;

    @Autowired
    private PointNourrissageMapper pointNourrissageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointNourrissageMockMvc;

    private PointNourrissage pointNourrissage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointNourrissage createEntity(EntityManager em) {
        PointNourrissage pointNourrissage = new PointNourrissage().nom(DEFAULT_NOM);
        return pointNourrissage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointNourrissage createUpdatedEntity(EntityManager em) {
        PointNourrissage pointNourrissage = new PointNourrissage().nom(UPDATED_NOM);
        return pointNourrissage;
    }

    @BeforeEach
    public void initTest() {
        pointNourrissage = createEntity(em);
    }

    @Test
    @Transactional
    void createPointNourrissage() throws Exception {
        int databaseSizeBeforeCreate = pointNourrissageRepository.findAll().size();
        // Create the PointNourrissage
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(pointNourrissage);
        restPointNourrissageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeCreate + 1);
        PointNourrissage testPointNourrissage = pointNourrissageList.get(pointNourrissageList.size() - 1);
        assertThat(testPointNourrissage.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createPointNourrissageWithExistingId() throws Exception {
        // Create the PointNourrissage with an existing ID
        pointNourrissage.setId(1L);
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(pointNourrissage);

        int databaseSizeBeforeCreate = pointNourrissageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointNourrissageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPointNourrissages() throws Exception {
        // Initialize the database
        pointNourrissageRepository.saveAndFlush(pointNourrissage);

        // Get all the pointNourrissageList
        restPointNourrissageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointNourrissage.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getPointNourrissage() throws Exception {
        // Initialize the database
        pointNourrissageRepository.saveAndFlush(pointNourrissage);

        // Get the pointNourrissage
        restPointNourrissageMockMvc
            .perform(get(ENTITY_API_URL_ID, pointNourrissage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointNourrissage.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingPointNourrissage() throws Exception {
        // Get the pointNourrissage
        restPointNourrissageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPointNourrissage() throws Exception {
        // Initialize the database
        pointNourrissageRepository.saveAndFlush(pointNourrissage);

        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();

        // Update the pointNourrissage
        PointNourrissage updatedPointNourrissage = pointNourrissageRepository.findById(pointNourrissage.getId()).get();
        // Disconnect from session so that the updates on updatedPointNourrissage are not directly saved in db
        em.detach(updatedPointNourrissage);
        updatedPointNourrissage.nom(UPDATED_NOM);
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(updatedPointNourrissage);

        restPointNourrissageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointNourrissageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isOk());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
        PointNourrissage testPointNourrissage = pointNourrissageList.get(pointNourrissageList.size() - 1);
        assertThat(testPointNourrissage.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingPointNourrissage() throws Exception {
        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();
        pointNourrissage.setId(count.incrementAndGet());

        // Create the PointNourrissage
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(pointNourrissage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointNourrissageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointNourrissageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointNourrissage() throws Exception {
        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();
        pointNourrissage.setId(count.incrementAndGet());

        // Create the PointNourrissage
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(pointNourrissage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointNourrissageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointNourrissage() throws Exception {
        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();
        pointNourrissage.setId(count.incrementAndGet());

        // Create the PointNourrissage
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(pointNourrissage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointNourrissageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointNourrissageWithPatch() throws Exception {
        // Initialize the database
        pointNourrissageRepository.saveAndFlush(pointNourrissage);

        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();

        // Update the pointNourrissage using partial update
        PointNourrissage partialUpdatedPointNourrissage = new PointNourrissage();
        partialUpdatedPointNourrissage.setId(pointNourrissage.getId());

        restPointNourrissageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointNourrissage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointNourrissage))
            )
            .andExpect(status().isOk());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
        PointNourrissage testPointNourrissage = pointNourrissageList.get(pointNourrissageList.size() - 1);
        assertThat(testPointNourrissage.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdatePointNourrissageWithPatch() throws Exception {
        // Initialize the database
        pointNourrissageRepository.saveAndFlush(pointNourrissage);

        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();

        // Update the pointNourrissage using partial update
        PointNourrissage partialUpdatedPointNourrissage = new PointNourrissage();
        partialUpdatedPointNourrissage.setId(pointNourrissage.getId());

        partialUpdatedPointNourrissage.nom(UPDATED_NOM);

        restPointNourrissageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointNourrissage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointNourrissage))
            )
            .andExpect(status().isOk());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
        PointNourrissage testPointNourrissage = pointNourrissageList.get(pointNourrissageList.size() - 1);
        assertThat(testPointNourrissage.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingPointNourrissage() throws Exception {
        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();
        pointNourrissage.setId(count.incrementAndGet());

        // Create the PointNourrissage
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(pointNourrissage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointNourrissageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointNourrissageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointNourrissage() throws Exception {
        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();
        pointNourrissage.setId(count.incrementAndGet());

        // Create the PointNourrissage
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(pointNourrissage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointNourrissageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointNourrissage() throws Exception {
        int databaseSizeBeforeUpdate = pointNourrissageRepository.findAll().size();
        pointNourrissage.setId(count.incrementAndGet());

        // Create the PointNourrissage
        PointNourrissageDTO pointNourrissageDTO = pointNourrissageMapper.toDto(pointNourrissage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointNourrissageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointNourrissageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointNourrissage in the database
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointNourrissage() throws Exception {
        // Initialize the database
        pointNourrissageRepository.saveAndFlush(pointNourrissage);

        int databaseSizeBeforeDelete = pointNourrissageRepository.findAll().size();

        // Delete the pointNourrissage
        restPointNourrissageMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointNourrissage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointNourrissage> pointNourrissageList = pointNourrissageRepository.findAll();
        assertThat(pointNourrissageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
