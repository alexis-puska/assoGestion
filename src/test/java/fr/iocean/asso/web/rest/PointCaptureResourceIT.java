package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.PointCapture;
import fr.iocean.asso.repository.PointCaptureRepository;
import fr.iocean.asso.service.dto.PointCaptureDTO;
import fr.iocean.asso.service.mapper.PointCaptureMapper;
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
 * Integration tests for the {@link PointCaptureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointCaptureResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/point-captures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointCaptureRepository pointCaptureRepository;

    @Autowired
    private PointCaptureMapper pointCaptureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointCaptureMockMvc;

    private PointCapture pointCapture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointCapture createEntity(EntityManager em) {
        PointCapture pointCapture = new PointCapture().nom(DEFAULT_NOM);
        return pointCapture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointCapture createUpdatedEntity(EntityManager em) {
        PointCapture pointCapture = new PointCapture().nom(UPDATED_NOM);
        return pointCapture;
    }

    @BeforeEach
    public void initTest() {
        pointCapture = createEntity(em);
    }

    @Test
    @Transactional
    void createPointCapture() throws Exception {
        int databaseSizeBeforeCreate = pointCaptureRepository.findAll().size();
        // Create the PointCapture
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(pointCapture);
        restPointCaptureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeCreate + 1);
        PointCapture testPointCapture = pointCaptureList.get(pointCaptureList.size() - 1);
        assertThat(testPointCapture.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createPointCaptureWithExistingId() throws Exception {
        // Create the PointCapture with an existing ID
        pointCapture.setId(1L);
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(pointCapture);

        int databaseSizeBeforeCreate = pointCaptureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointCaptureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPointCaptures() throws Exception {
        // Initialize the database
        pointCaptureRepository.saveAndFlush(pointCapture);

        // Get all the pointCaptureList
        restPointCaptureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointCapture.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getPointCapture() throws Exception {
        // Initialize the database
        pointCaptureRepository.saveAndFlush(pointCapture);

        // Get the pointCapture
        restPointCaptureMockMvc
            .perform(get(ENTITY_API_URL_ID, pointCapture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointCapture.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingPointCapture() throws Exception {
        // Get the pointCapture
        restPointCaptureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPointCapture() throws Exception {
        // Initialize the database
        pointCaptureRepository.saveAndFlush(pointCapture);

        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();

        // Update the pointCapture
        PointCapture updatedPointCapture = pointCaptureRepository.findById(pointCapture.getId()).get();
        // Disconnect from session so that the updates on updatedPointCapture are not directly saved in db
        em.detach(updatedPointCapture);
        updatedPointCapture.nom(UPDATED_NOM);
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(updatedPointCapture);

        restPointCaptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointCaptureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isOk());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
        PointCapture testPointCapture = pointCaptureList.get(pointCaptureList.size() - 1);
        assertThat(testPointCapture.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingPointCapture() throws Exception {
        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();
        pointCapture.setId(count.incrementAndGet());

        // Create the PointCapture
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(pointCapture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointCaptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointCaptureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointCapture() throws Exception {
        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();
        pointCapture.setId(count.incrementAndGet());

        // Create the PointCapture
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(pointCapture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointCaptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointCapture() throws Exception {
        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();
        pointCapture.setId(count.incrementAndGet());

        // Create the PointCapture
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(pointCapture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointCaptureMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointCaptureWithPatch() throws Exception {
        // Initialize the database
        pointCaptureRepository.saveAndFlush(pointCapture);

        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();

        // Update the pointCapture using partial update
        PointCapture partialUpdatedPointCapture = new PointCapture();
        partialUpdatedPointCapture.setId(pointCapture.getId());

        restPointCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointCapture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointCapture))
            )
            .andExpect(status().isOk());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
        PointCapture testPointCapture = pointCaptureList.get(pointCaptureList.size() - 1);
        assertThat(testPointCapture.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdatePointCaptureWithPatch() throws Exception {
        // Initialize the database
        pointCaptureRepository.saveAndFlush(pointCapture);

        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();

        // Update the pointCapture using partial update
        PointCapture partialUpdatedPointCapture = new PointCapture();
        partialUpdatedPointCapture.setId(pointCapture.getId());

        partialUpdatedPointCapture.nom(UPDATED_NOM);

        restPointCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointCapture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointCapture))
            )
            .andExpect(status().isOk());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
        PointCapture testPointCapture = pointCaptureList.get(pointCaptureList.size() - 1);
        assertThat(testPointCapture.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingPointCapture() throws Exception {
        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();
        pointCapture.setId(count.incrementAndGet());

        // Create the PointCapture
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(pointCapture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointCaptureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointCapture() throws Exception {
        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();
        pointCapture.setId(count.incrementAndGet());

        // Create the PointCapture
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(pointCapture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointCapture() throws Exception {
        int databaseSizeBeforeUpdate = pointCaptureRepository.findAll().size();
        pointCapture.setId(count.incrementAndGet());

        // Create the PointCapture
        PointCaptureDTO pointCaptureDTO = pointCaptureMapper.toDto(pointCapture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointCaptureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointCapture in the database
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointCapture() throws Exception {
        // Initialize the database
        pointCaptureRepository.saveAndFlush(pointCapture);

        int databaseSizeBeforeDelete = pointCaptureRepository.findAll().size();

        // Delete the pointCapture
        restPointCaptureMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointCapture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointCapture> pointCaptureList = pointCaptureRepository.findAll();
        assertThat(pointCaptureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
