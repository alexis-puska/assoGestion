package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.FamilleAccueil;
import fr.iocean.asso.domain.enumeration.TypeLogementEnum;
import fr.iocean.asso.repository.FamilleAccueilRepository;
import fr.iocean.asso.service.dto.FamilleAccueilDTO;
import fr.iocean.asso.service.mapper.FamilleAccueilMapper;
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
 * Integration tests for the {@link FamilleAccueilResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilleAccueilResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final TypeLogementEnum DEFAULT_TYPE_LOGEMENT = TypeLogementEnum.MAISON;
    private static final TypeLogementEnum UPDATED_TYPE_LOGEMENT = TypeLogementEnum.APPARTEMENT;

    private static final Integer DEFAULT_NOMBRE_PIECE = 1;
    private static final Integer UPDATED_NOMBRE_PIECE = 2;

    private static final Integer DEFAULT_NOMBRE_CHAT = 1;
    private static final Integer UPDATED_NOMBRE_CHAT = 2;

    private static final Integer DEFAULT_NOMBRE_CHIEN = 1;
    private static final Integer UPDATED_NOMBRE_CHIEN = 2;

    private static final String ENTITY_API_URL = "/api/famille-accueils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilleAccueilRepository familleAccueilRepository;

    @Autowired
    private FamilleAccueilMapper familleAccueilMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilleAccueilMockMvc;

    private FamilleAccueil familleAccueil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilleAccueil createEntity(EntityManager em) {
        FamilleAccueil familleAccueil = new FamilleAccueil()
            .nom(DEFAULT_NOM)
            .typeLogement(DEFAULT_TYPE_LOGEMENT)
            .nombrePiece(DEFAULT_NOMBRE_PIECE)
            .nombreChat(DEFAULT_NOMBRE_CHAT)
            .nombreChien(DEFAULT_NOMBRE_CHIEN);
        return familleAccueil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilleAccueil createUpdatedEntity(EntityManager em) {
        FamilleAccueil familleAccueil = new FamilleAccueil()
            .nom(UPDATED_NOM)
            .typeLogement(UPDATED_TYPE_LOGEMENT)
            .nombrePiece(UPDATED_NOMBRE_PIECE)
            .nombreChat(UPDATED_NOMBRE_CHAT)
            .nombreChien(UPDATED_NOMBRE_CHIEN);
        return familleAccueil;
    }

    @BeforeEach
    public void initTest() {
        familleAccueil = createEntity(em);
    }

    @Test
    @Transactional
    void createFamilleAccueil() throws Exception {
        int databaseSizeBeforeCreate = familleAccueilRepository.findAll().size();
        // Create the FamilleAccueil
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(familleAccueil);
        restFamilleAccueilMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeCreate + 1);
        FamilleAccueil testFamilleAccueil = familleAccueilList.get(familleAccueilList.size() - 1);
        assertThat(testFamilleAccueil.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFamilleAccueil.getTypeLogement()).isEqualTo(DEFAULT_TYPE_LOGEMENT);
        assertThat(testFamilleAccueil.getNombrePiece()).isEqualTo(DEFAULT_NOMBRE_PIECE);
        assertThat(testFamilleAccueil.getNombreChat()).isEqualTo(DEFAULT_NOMBRE_CHAT);
        assertThat(testFamilleAccueil.getNombreChien()).isEqualTo(DEFAULT_NOMBRE_CHIEN);
    }

    @Test
    @Transactional
    void createFamilleAccueilWithExistingId() throws Exception {
        // Create the FamilleAccueil with an existing ID
        familleAccueil.setId(1L);
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(familleAccueil);

        int databaseSizeBeforeCreate = familleAccueilRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilleAccueilMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFamilleAccueils() throws Exception {
        // Initialize the database
        familleAccueilRepository.saveAndFlush(familleAccueil);

        // Get all the familleAccueilList
        restFamilleAccueilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familleAccueil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].typeLogement").value(hasItem(DEFAULT_TYPE_LOGEMENT.toString())))
            .andExpect(jsonPath("$.[*].nombrePiece").value(hasItem(DEFAULT_NOMBRE_PIECE)))
            .andExpect(jsonPath("$.[*].nombreChat").value(hasItem(DEFAULT_NOMBRE_CHAT)))
            .andExpect(jsonPath("$.[*].nombreChien").value(hasItem(DEFAULT_NOMBRE_CHIEN)));
    }

    @Test
    @Transactional
    void getFamilleAccueil() throws Exception {
        // Initialize the database
        familleAccueilRepository.saveAndFlush(familleAccueil);

        // Get the familleAccueil
        restFamilleAccueilMockMvc
            .perform(get(ENTITY_API_URL_ID, familleAccueil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familleAccueil.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.typeLogement").value(DEFAULT_TYPE_LOGEMENT.toString()))
            .andExpect(jsonPath("$.nombrePiece").value(DEFAULT_NOMBRE_PIECE))
            .andExpect(jsonPath("$.nombreChat").value(DEFAULT_NOMBRE_CHAT))
            .andExpect(jsonPath("$.nombreChien").value(DEFAULT_NOMBRE_CHIEN));
    }

    @Test
    @Transactional
    void getNonExistingFamilleAccueil() throws Exception {
        // Get the familleAccueil
        restFamilleAccueilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFamilleAccueil() throws Exception {
        // Initialize the database
        familleAccueilRepository.saveAndFlush(familleAccueil);

        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();

        // Update the familleAccueil
        FamilleAccueil updatedFamilleAccueil = familleAccueilRepository.findById(familleAccueil.getId()).get();
        // Disconnect from session so that the updates on updatedFamilleAccueil are not directly saved in db
        em.detach(updatedFamilleAccueil);
        updatedFamilleAccueil
            .nom(UPDATED_NOM)
            .typeLogement(UPDATED_TYPE_LOGEMENT)
            .nombrePiece(UPDATED_NOMBRE_PIECE)
            .nombreChat(UPDATED_NOMBRE_CHAT)
            .nombreChien(UPDATED_NOMBRE_CHIEN);
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(updatedFamilleAccueil);

        restFamilleAccueilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familleAccueilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isOk());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
        FamilleAccueil testFamilleAccueil = familleAccueilList.get(familleAccueilList.size() - 1);
        assertThat(testFamilleAccueil.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFamilleAccueil.getTypeLogement()).isEqualTo(UPDATED_TYPE_LOGEMENT);
        assertThat(testFamilleAccueil.getNombrePiece()).isEqualTo(UPDATED_NOMBRE_PIECE);
        assertThat(testFamilleAccueil.getNombreChat()).isEqualTo(UPDATED_NOMBRE_CHAT);
        assertThat(testFamilleAccueil.getNombreChien()).isEqualTo(UPDATED_NOMBRE_CHIEN);
    }

    @Test
    @Transactional
    void putNonExistingFamilleAccueil() throws Exception {
        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();
        familleAccueil.setId(count.incrementAndGet());

        // Create the FamilleAccueil
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(familleAccueil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilleAccueilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familleAccueilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilleAccueil() throws Exception {
        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();
        familleAccueil.setId(count.incrementAndGet());

        // Create the FamilleAccueil
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(familleAccueil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleAccueilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilleAccueil() throws Exception {
        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();
        familleAccueil.setId(count.incrementAndGet());

        // Create the FamilleAccueil
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(familleAccueil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleAccueilMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilleAccueilWithPatch() throws Exception {
        // Initialize the database
        familleAccueilRepository.saveAndFlush(familleAccueil);

        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();

        // Update the familleAccueil using partial update
        FamilleAccueil partialUpdatedFamilleAccueil = new FamilleAccueil();
        partialUpdatedFamilleAccueil.setId(familleAccueil.getId());

        partialUpdatedFamilleAccueil.nom(UPDATED_NOM).nombreChat(UPDATED_NOMBRE_CHAT).nombreChien(UPDATED_NOMBRE_CHIEN);

        restFamilleAccueilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilleAccueil.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilleAccueil))
            )
            .andExpect(status().isOk());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
        FamilleAccueil testFamilleAccueil = familleAccueilList.get(familleAccueilList.size() - 1);
        assertThat(testFamilleAccueil.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFamilleAccueil.getTypeLogement()).isEqualTo(DEFAULT_TYPE_LOGEMENT);
        assertThat(testFamilleAccueil.getNombrePiece()).isEqualTo(DEFAULT_NOMBRE_PIECE);
        assertThat(testFamilleAccueil.getNombreChat()).isEqualTo(UPDATED_NOMBRE_CHAT);
        assertThat(testFamilleAccueil.getNombreChien()).isEqualTo(UPDATED_NOMBRE_CHIEN);
    }

    @Test
    @Transactional
    void fullUpdateFamilleAccueilWithPatch() throws Exception {
        // Initialize the database
        familleAccueilRepository.saveAndFlush(familleAccueil);

        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();

        // Update the familleAccueil using partial update
        FamilleAccueil partialUpdatedFamilleAccueil = new FamilleAccueil();
        partialUpdatedFamilleAccueil.setId(familleAccueil.getId());

        partialUpdatedFamilleAccueil
            .nom(UPDATED_NOM)
            .typeLogement(UPDATED_TYPE_LOGEMENT)
            .nombrePiece(UPDATED_NOMBRE_PIECE)
            .nombreChat(UPDATED_NOMBRE_CHAT)
            .nombreChien(UPDATED_NOMBRE_CHIEN);

        restFamilleAccueilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilleAccueil.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilleAccueil))
            )
            .andExpect(status().isOk());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
        FamilleAccueil testFamilleAccueil = familleAccueilList.get(familleAccueilList.size() - 1);
        assertThat(testFamilleAccueil.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFamilleAccueil.getTypeLogement()).isEqualTo(UPDATED_TYPE_LOGEMENT);
        assertThat(testFamilleAccueil.getNombrePiece()).isEqualTo(UPDATED_NOMBRE_PIECE);
        assertThat(testFamilleAccueil.getNombreChat()).isEqualTo(UPDATED_NOMBRE_CHAT);
        assertThat(testFamilleAccueil.getNombreChien()).isEqualTo(UPDATED_NOMBRE_CHIEN);
    }

    @Test
    @Transactional
    void patchNonExistingFamilleAccueil() throws Exception {
        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();
        familleAccueil.setId(count.incrementAndGet());

        // Create the FamilleAccueil
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(familleAccueil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilleAccueilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familleAccueilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilleAccueil() throws Exception {
        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();
        familleAccueil.setId(count.incrementAndGet());

        // Create the FamilleAccueil
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(familleAccueil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleAccueilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilleAccueil() throws Exception {
        int databaseSizeBeforeUpdate = familleAccueilRepository.findAll().size();
        familleAccueil.setId(count.incrementAndGet());

        // Create the FamilleAccueil
        FamilleAccueilDTO familleAccueilDTO = familleAccueilMapper.toDto(familleAccueil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleAccueilMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familleAccueilDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilleAccueil in the database
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilleAccueil() throws Exception {
        // Initialize the database
        familleAccueilRepository.saveAndFlush(familleAccueil);

        int databaseSizeBeforeDelete = familleAccueilRepository.findAll().size();

        // Delete the familleAccueil
        restFamilleAccueilMockMvc
            .perform(delete(ENTITY_API_URL_ID, familleAccueil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FamilleAccueil> familleAccueilList = familleAccueilRepository.findAll();
        assertThat(familleAccueilList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
