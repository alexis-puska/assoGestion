package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.ActeVeterinaire;
import fr.iocean.asso.repository.ActeVeterinaireRepository;
import fr.iocean.asso.service.dto.ActeVeterinaireDTO;
import fr.iocean.asso.service.mapper.ActeVeterinaireMapper;
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
 * Integration tests for the {@link ActeVeterinaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActeVeterinaireResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acte-veterinaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActeVeterinaireRepository acteVeterinaireRepository;

    @Autowired
    private ActeVeterinaireMapper acteVeterinaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActeVeterinaireMockMvc;

    private ActeVeterinaire acteVeterinaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActeVeterinaire createEntity(EntityManager em) {
        ActeVeterinaire acteVeterinaire = new ActeVeterinaire().libelle(DEFAULT_LIBELLE);
        return acteVeterinaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActeVeterinaire createUpdatedEntity(EntityManager em) {
        ActeVeterinaire acteVeterinaire = new ActeVeterinaire().libelle(UPDATED_LIBELLE);
        return acteVeterinaire;
    }

    @BeforeEach
    public void initTest() {
        acteVeterinaire = createEntity(em);
    }

    @Test
    @Transactional
    void createActeVeterinaire() throws Exception {
        int databaseSizeBeforeCreate = acteVeterinaireRepository.findAll().size();
        // Create the ActeVeterinaire
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(acteVeterinaire);
        restActeVeterinaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeCreate + 1);
        ActeVeterinaire testActeVeterinaire = acteVeterinaireList.get(acteVeterinaireList.size() - 1);
        assertThat(testActeVeterinaire.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createActeVeterinaireWithExistingId() throws Exception {
        // Create the ActeVeterinaire with an existing ID
        acteVeterinaire.setId(1L);
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(acteVeterinaire);

        int databaseSizeBeforeCreate = acteVeterinaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActeVeterinaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActeVeterinaires() throws Exception {
        // Initialize the database
        acteVeterinaireRepository.saveAndFlush(acteVeterinaire);

        // Get all the acteVeterinaireList
        restActeVeterinaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acteVeterinaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getActeVeterinaire() throws Exception {
        // Initialize the database
        acteVeterinaireRepository.saveAndFlush(acteVeterinaire);

        // Get the acteVeterinaire
        restActeVeterinaireMockMvc
            .perform(get(ENTITY_API_URL_ID, acteVeterinaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acteVeterinaire.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingActeVeterinaire() throws Exception {
        // Get the acteVeterinaire
        restActeVeterinaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewActeVeterinaire() throws Exception {
        // Initialize the database
        acteVeterinaireRepository.saveAndFlush(acteVeterinaire);

        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();

        // Update the acteVeterinaire
        ActeVeterinaire updatedActeVeterinaire = acteVeterinaireRepository.findById(acteVeterinaire.getId()).get();
        // Disconnect from session so that the updates on updatedActeVeterinaire are not directly saved in db
        em.detach(updatedActeVeterinaire);
        updatedActeVeterinaire.libelle(UPDATED_LIBELLE);
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(updatedActeVeterinaire);

        restActeVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acteVeterinaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        ActeVeterinaire testActeVeterinaire = acteVeterinaireList.get(acteVeterinaireList.size() - 1);
        assertThat(testActeVeterinaire.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingActeVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();
        acteVeterinaire.setId(count.incrementAndGet());

        // Create the ActeVeterinaire
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(acteVeterinaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActeVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acteVeterinaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActeVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();
        acteVeterinaire.setId(count.incrementAndGet());

        // Create the ActeVeterinaire
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(acteVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActeVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActeVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();
        acteVeterinaire.setId(count.incrementAndGet());

        // Create the ActeVeterinaire
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(acteVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActeVeterinaireMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActeVeterinaireWithPatch() throws Exception {
        // Initialize the database
        acteVeterinaireRepository.saveAndFlush(acteVeterinaire);

        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();

        // Update the acteVeterinaire using partial update
        ActeVeterinaire partialUpdatedActeVeterinaire = new ActeVeterinaire();
        partialUpdatedActeVeterinaire.setId(acteVeterinaire.getId());

        partialUpdatedActeVeterinaire.libelle(UPDATED_LIBELLE);

        restActeVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActeVeterinaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActeVeterinaire))
            )
            .andExpect(status().isOk());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        ActeVeterinaire testActeVeterinaire = acteVeterinaireList.get(acteVeterinaireList.size() - 1);
        assertThat(testActeVeterinaire.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateActeVeterinaireWithPatch() throws Exception {
        // Initialize the database
        acteVeterinaireRepository.saveAndFlush(acteVeterinaire);

        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();

        // Update the acteVeterinaire using partial update
        ActeVeterinaire partialUpdatedActeVeterinaire = new ActeVeterinaire();
        partialUpdatedActeVeterinaire.setId(acteVeterinaire.getId());

        partialUpdatedActeVeterinaire.libelle(UPDATED_LIBELLE);

        restActeVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActeVeterinaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActeVeterinaire))
            )
            .andExpect(status().isOk());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
        ActeVeterinaire testActeVeterinaire = acteVeterinaireList.get(acteVeterinaireList.size() - 1);
        assertThat(testActeVeterinaire.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingActeVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();
        acteVeterinaire.setId(count.incrementAndGet());

        // Create the ActeVeterinaire
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(acteVeterinaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActeVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acteVeterinaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActeVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();
        acteVeterinaire.setId(count.incrementAndGet());

        // Create the ActeVeterinaire
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(acteVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActeVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActeVeterinaire() throws Exception {
        int databaseSizeBeforeUpdate = acteVeterinaireRepository.findAll().size();
        acteVeterinaire.setId(count.incrementAndGet());

        // Create the ActeVeterinaire
        ActeVeterinaireDTO acteVeterinaireDTO = acteVeterinaireMapper.toDto(acteVeterinaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActeVeterinaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acteVeterinaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActeVeterinaire in the database
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActeVeterinaire() throws Exception {
        // Initialize the database
        acteVeterinaireRepository.saveAndFlush(acteVeterinaire);

        int databaseSizeBeforeDelete = acteVeterinaireRepository.findAll().size();

        // Delete the acteVeterinaire
        restActeVeterinaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, acteVeterinaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActeVeterinaire> acteVeterinaireList = acteVeterinaireRepository.findAll();
        assertThat(acteVeterinaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
