package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.ConfigurationDon;
import fr.iocean.asso.repository.ConfigurationDonRepository;
import fr.iocean.asso.service.dto.ConfigurationDonDTO;
import fr.iocean.asso.service.mapper.ConfigurationDonMapper;
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
 * Integration tests for the {@link ConfigurationDonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigurationDonResourceIT {

    private static final String DEFAULT_DENOMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final String DEFAULT_SIGNATAIRE = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/configuration-dons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfigurationDonRepository configurationDonRepository;

    @Autowired
    private ConfigurationDonMapper configurationDonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigurationDonMockMvc;

    private ConfigurationDon configurationDon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationDon createEntity(EntityManager em) {
        ConfigurationDon configurationDon = new ConfigurationDon()
            .denomination(DEFAULT_DENOMINATION)
            .objet(DEFAULT_OBJET)
            .signataire(DEFAULT_SIGNATAIRE);
        return configurationDon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationDon createUpdatedEntity(EntityManager em) {
        ConfigurationDon configurationDon = new ConfigurationDon()
            .denomination(UPDATED_DENOMINATION)
            .objet(UPDATED_OBJET)
            .signataire(UPDATED_SIGNATAIRE);
        return configurationDon;
    }

    @BeforeEach
    public void initTest() {
        configurationDon = createEntity(em);
    }

    @Test
    @Transactional
    void createConfigurationDon() throws Exception {
        int databaseSizeBeforeCreate = configurationDonRepository.findAll().size();
        // Create the ConfigurationDon
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(configurationDon);
        restConfigurationDonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigurationDon testConfigurationDon = configurationDonList.get(configurationDonList.size() - 1);
        assertThat(testConfigurationDon.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testConfigurationDon.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testConfigurationDon.getSignataire()).isEqualTo(DEFAULT_SIGNATAIRE);
    }

    @Test
    @Transactional
    void createConfigurationDonWithExistingId() throws Exception {
        // Create the ConfigurationDon with an existing ID
        configurationDon.setId(1L);
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(configurationDon);

        int databaseSizeBeforeCreate = configurationDonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationDonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConfigurationDons() throws Exception {
        // Initialize the database
        configurationDonRepository.saveAndFlush(configurationDon);

        // Get all the configurationDonList
        restConfigurationDonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationDon.getId().intValue())))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].signataire").value(hasItem(DEFAULT_SIGNATAIRE)));
    }

    @Test
    @Transactional
    void getConfigurationDon() throws Exception {
        // Initialize the database
        configurationDonRepository.saveAndFlush(configurationDon);

        // Get the configurationDon
        restConfigurationDonMockMvc
            .perform(get(ENTITY_API_URL_ID, configurationDon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configurationDon.getId().intValue()))
            .andExpect(jsonPath("$.denomination").value(DEFAULT_DENOMINATION))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.signataire").value(DEFAULT_SIGNATAIRE));
    }

    @Test
    @Transactional
    void getNonExistingConfigurationDon() throws Exception {
        // Get the configurationDon
        restConfigurationDonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConfigurationDon() throws Exception {
        // Initialize the database
        configurationDonRepository.saveAndFlush(configurationDon);

        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();

        // Update the configurationDon
        ConfigurationDon updatedConfigurationDon = configurationDonRepository.findById(configurationDon.getId()).get();
        // Disconnect from session so that the updates on updatedConfigurationDon are not directly saved in db
        em.detach(updatedConfigurationDon);
        updatedConfigurationDon.denomination(UPDATED_DENOMINATION).objet(UPDATED_OBJET).signataire(UPDATED_SIGNATAIRE);
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(updatedConfigurationDon);

        restConfigurationDonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configurationDonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationDon testConfigurationDon = configurationDonList.get(configurationDonList.size() - 1);
        assertThat(testConfigurationDon.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testConfigurationDon.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testConfigurationDon.getSignataire()).isEqualTo(UPDATED_SIGNATAIRE);
    }

    @Test
    @Transactional
    void putNonExistingConfigurationDon() throws Exception {
        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();
        configurationDon.setId(count.incrementAndGet());

        // Create the ConfigurationDon
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(configurationDon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationDonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configurationDonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfigurationDon() throws Exception {
        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();
        configurationDon.setId(count.incrementAndGet());

        // Create the ConfigurationDon
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(configurationDon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationDonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfigurationDon() throws Exception {
        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();
        configurationDon.setId(count.incrementAndGet());

        // Create the ConfigurationDon
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(configurationDon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationDonMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigurationDonWithPatch() throws Exception {
        // Initialize the database
        configurationDonRepository.saveAndFlush(configurationDon);

        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();

        // Update the configurationDon using partial update
        ConfigurationDon partialUpdatedConfigurationDon = new ConfigurationDon();
        partialUpdatedConfigurationDon.setId(configurationDon.getId());

        partialUpdatedConfigurationDon.denomination(UPDATED_DENOMINATION).objet(UPDATED_OBJET);

        restConfigurationDonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigurationDon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigurationDon))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationDon testConfigurationDon = configurationDonList.get(configurationDonList.size() - 1);
        assertThat(testConfigurationDon.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testConfigurationDon.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testConfigurationDon.getSignataire()).isEqualTo(DEFAULT_SIGNATAIRE);
    }

    @Test
    @Transactional
    void fullUpdateConfigurationDonWithPatch() throws Exception {
        // Initialize the database
        configurationDonRepository.saveAndFlush(configurationDon);

        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();

        // Update the configurationDon using partial update
        ConfigurationDon partialUpdatedConfigurationDon = new ConfigurationDon();
        partialUpdatedConfigurationDon.setId(configurationDon.getId());

        partialUpdatedConfigurationDon.denomination(UPDATED_DENOMINATION).objet(UPDATED_OBJET).signataire(UPDATED_SIGNATAIRE);

        restConfigurationDonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigurationDon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigurationDon))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationDon testConfigurationDon = configurationDonList.get(configurationDonList.size() - 1);
        assertThat(testConfigurationDon.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testConfigurationDon.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testConfigurationDon.getSignataire()).isEqualTo(UPDATED_SIGNATAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingConfigurationDon() throws Exception {
        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();
        configurationDon.setId(count.incrementAndGet());

        // Create the ConfigurationDon
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(configurationDon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationDonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configurationDonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfigurationDon() throws Exception {
        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();
        configurationDon.setId(count.incrementAndGet());

        // Create the ConfigurationDon
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(configurationDon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationDonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfigurationDon() throws Exception {
        int databaseSizeBeforeUpdate = configurationDonRepository.findAll().size();
        configurationDon.setId(count.incrementAndGet());

        // Create the ConfigurationDon
        ConfigurationDonDTO configurationDonDTO = configurationDonMapper.toDto(configurationDon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationDonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationDonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigurationDon in the database
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfigurationDon() throws Exception {
        // Initialize the database
        configurationDonRepository.saveAndFlush(configurationDon);

        int databaseSizeBeforeDelete = configurationDonRepository.findAll().size();

        // Delete the configurationDon
        restConfigurationDonMockMvc
            .perform(delete(ENTITY_API_URL_ID, configurationDon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigurationDon> configurationDonList = configurationDonRepository.findAll();
        assertThat(configurationDonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
