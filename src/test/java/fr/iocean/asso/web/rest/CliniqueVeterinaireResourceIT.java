package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.CliniqueVeterinaire;
import fr.iocean.asso.repository.CliniqueVeterinaireRepository;
import fr.iocean.asso.service.dto.CliniqueVeterinaireDTO;
import fr.iocean.asso.service.mapper.CliniqueVeterinaireMapper;
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
 * Integration tests for the {@link CliniqueVeterinaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CliniqueVeterinaireResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/clinique-veterinaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CliniqueVeterinaireRepository cliniqueVeterinaireRepository;

    @Autowired
    private CliniqueVeterinaireMapper cliniqueVeterinaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCliniqueVeterinaireMockMvc;

    private CliniqueVeterinaire cliniqueVeterinaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CliniqueVeterinaire createEntity(EntityManager em) {
        CliniqueVeterinaire cliniqueVeterinaire = new CliniqueVeterinaire().nom(DEFAULT_NOM).actif(DEFAULT_ACTIF);
        return cliniqueVeterinaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CliniqueVeterinaire createUpdatedEntity(EntityManager em) {
        CliniqueVeterinaire cliniqueVeterinaire = new CliniqueVeterinaire().nom(UPDATED_NOM).actif(UPDATED_ACTIF);
        return cliniqueVeterinaire;
    }

    @BeforeEach
    public void initTest() {
        cliniqueVeterinaire = createEntity(em);
    }

    @Test
    @Transactional
    void createCliniqueVeterinaire() throws Exception {
        int databaseSizeBeforeCreate = cliniqueVeterinaireRepository.findAll().size();
        // Create the CliniqueVeterinaire
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);
        restCliniqueVeterinaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeCreate + 1);
        CliniqueVeterinaire testCliniqueVeterinaire = cliniqueVeterinaireList.get(cliniqueVeterinaireList.size() - 1);
        assertThat(testCliniqueVeterinaire.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCliniqueVeterinaire.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void createCliniqueVeterinaireWithExistingId() throws Exception {
        // Create the CliniqueVeterinaire with an existing ID
        cliniqueVeterinaire.setId(1L);
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);

        int databaseSizeBeforeCreate = cliniqueVeterinaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCliniqueVeterinaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCliniqueVeterinaires() throws Exception {
        // Initialize the database
        cliniqueVeterinaireRepository.saveAndFlush(cliniqueVeterinaire);

        // Get all the cliniqueVeterinaireList
        restCliniqueVeterinaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliniqueVeterinaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    void getCliniqueVeterinaire() throws Exception {
        // Initialize the database
        cliniqueVeterinaireRepository.saveAndFlush(cliniqueVeterinaire);

        // Get the cliniqueVeterinaire
        restCliniqueVeterinaireMockMvc
            .perform(get(ENTITY_API_URL_ID, cliniqueVeterinaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cliniqueVeterinaire.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCliniqueVeterinaire() throws Exception {
        // Get the cliniqueVeterinaire
        restCliniqueVeterinaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCliniqueVeterinaire() throws Exception {
        // Initialize the database
        cliniqueVeterinaireRepository.saveAndFlush(cliniqueVeterinaire);

        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();

        // Update the cliniqueVeterinaire
        CliniqueVeterinaire updatedCliniqueVeterinaire = cliniqueVeterinaireRepository.findById(cliniqueVeterinaire.getId()).get();
        // Disconnect from session so that the updates on updatedCliniqueVeterinaire are not directly saved in db
        em.detach(updatedCliniqueVeterinaire);
        updatedCliniqueVeterinaire.nom(UPDATED_NOM).actif(UPDATED_ACTIF);
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(updatedCliniqueVeterinaire);

        restCliniqueVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cliniqueVeterinaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        CliniqueVeterinaire testCliniqueVeterinaire = cliniqueVeterinaireList.get(cliniqueVeterinaireList.size() - 1);
        assertThat(testCliniqueVeterinaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCliniqueVeterinaire.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void putNonExistingCliniqueVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();
        cliniqueVeterinaire.setId(count.incrementAndGet());

        // Create the CliniqueVeterinaire
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCliniqueVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cliniqueVeterinaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCliniqueVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();
        cliniqueVeterinaire.setId(count.incrementAndGet());

        // Create the CliniqueVeterinaire
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCliniqueVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCliniqueVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();
        cliniqueVeterinaire.setId(count.incrementAndGet());

        // Create the CliniqueVeterinaire
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCliniqueVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCliniqueVeterinaireWithPatch() throws Exception {
        // Initialize the database
        cliniqueVeterinaireRepository.saveAndFlush(cliniqueVeterinaire);

        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();

        // Update the cliniqueVeterinaire using partial update
        CliniqueVeterinaire partialUpdatedCliniqueVeterinaire = new CliniqueVeterinaire();
        partialUpdatedCliniqueVeterinaire.setId(cliniqueVeterinaire.getId());

        partialUpdatedCliniqueVeterinaire.nom(UPDATED_NOM);

        restCliniqueVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliniqueVeterinaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCliniqueVeterinaire))
            )
            .andExpect(status().isOk());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        CliniqueVeterinaire testCliniqueVeterinaire = cliniqueVeterinaireList.get(cliniqueVeterinaireList.size() - 1);
        assertThat(testCliniqueVeterinaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCliniqueVeterinaire.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void fullUpdateCliniqueVeterinaireWithPatch() throws Exception {
        // Initialize the database
        cliniqueVeterinaireRepository.saveAndFlush(cliniqueVeterinaire);

        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();

        // Update the cliniqueVeterinaire using partial update
        CliniqueVeterinaire partialUpdatedCliniqueVeterinaire = new CliniqueVeterinaire();
        partialUpdatedCliniqueVeterinaire.setId(cliniqueVeterinaire.getId());

        partialUpdatedCliniqueVeterinaire.nom(UPDATED_NOM).actif(UPDATED_ACTIF);

        restCliniqueVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliniqueVeterinaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCliniqueVeterinaire))
            )
            .andExpect(status().isOk());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        CliniqueVeterinaire testCliniqueVeterinaire = cliniqueVeterinaireList.get(cliniqueVeterinaireList.size() - 1);
        assertThat(testCliniqueVeterinaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCliniqueVeterinaire.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void patchNonExistingCliniqueVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();
        cliniqueVeterinaire.setId(count.incrementAndGet());

        // Create the CliniqueVeterinaire
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCliniqueVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cliniqueVeterinaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCliniqueVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();
        cliniqueVeterinaire.setId(count.incrementAndGet());

        // Create the CliniqueVeterinaire
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCliniqueVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCliniqueVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = cliniqueVeterinaireRepository.findAll().size();
        cliniqueVeterinaire.setId(count.incrementAndGet());

        // Create the CliniqueVeterinaire
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO = cliniqueVeterinaireMapper.toDto(cliniqueVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCliniqueVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cliniqueVeterinaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CliniqueVeterinaire in the database
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCliniqueVeterinaire() throws Exception {
        // Initialize the database
        cliniqueVeterinaireRepository.saveAndFlush(cliniqueVeterinaire);

        int databaseSizeBeforeDelete = cliniqueVeterinaireRepository.findAll().size();

        // Delete the cliniqueVeterinaire
        restCliniqueVeterinaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, cliniqueVeterinaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CliniqueVeterinaire> cliniqueVeterinaireList = cliniqueVeterinaireRepository.findAll();
        assertThat(cliniqueVeterinaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
