package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.ConfigurationAsso;
import fr.iocean.asso.repository.ConfigurationAssoRepository;
import fr.iocean.asso.service.dto.ConfigurationAssoDTO;
import fr.iocean.asso.service.mapper.ConfigurationAssoMapper;
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
 * Integration tests for the {@link ConfigurationAssoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigurationAssoResourceIT {

    private static final String DEFAULT_DENOMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final String DEFAULT_OBJET1 = "AAAAAAAAAA";
    private static final String UPDATED_OBJET1 = "BBBBBBBBBB";

    private static final String DEFAULT_OBJET2 = "AAAAAAAAAA";
    private static final String UPDATED_OBJET2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/configuration-assos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfigurationAssoRepository configurationAssoRepository;

    @Autowired
    private ConfigurationAssoMapper configurationAssoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigurationAssoMockMvc;

    private ConfigurationAsso configurationAsso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationAsso createEntity(EntityManager em) {
        ConfigurationAsso configurationAsso = new ConfigurationAsso()
            .denomination(DEFAULT_DENOMINATION)
            .objet(DEFAULT_OBJET)
            .objet1(DEFAULT_OBJET1)
            .objet2(DEFAULT_OBJET2);
        return configurationAsso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationAsso createUpdatedEntity(EntityManager em) {
        ConfigurationAsso configurationAsso = new ConfigurationAsso()
            .denomination(UPDATED_DENOMINATION)
            .objet(UPDATED_OBJET)
            .objet1(UPDATED_OBJET1)
            .objet2(UPDATED_OBJET2);
        return configurationAsso;
    }

    @BeforeEach
    public void initTest() {
        configurationAsso = createEntity(em);
    }

    @Test
    @Transactional
    void createConfigurationAsso() throws Exception {
        int databaseSizeBeforeCreate = configurationAssoRepository.findAll().size();
        // Create the ConfigurationAsso
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(configurationAsso);
        restConfigurationAssoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigurationAsso testConfigurationAsso = configurationAssoList.get(configurationAssoList.size() - 1);
        assertThat(testConfigurationAsso.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testConfigurationAsso.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testConfigurationAsso.getObjet1()).isEqualTo(DEFAULT_OBJET1);
        assertThat(testConfigurationAsso.getObjet2()).isEqualTo(DEFAULT_OBJET2);
    }

    @Test
    @Transactional
    void createConfigurationAssoWithExistingId() throws Exception {
        // Create the ConfigurationAsso with an existing ID
        configurationAsso.setId(1L);
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(configurationAsso);

        int databaseSizeBeforeCreate = configurationAssoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationAssoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConfigurationAssos() throws Exception {
        // Initialize the database
        configurationAssoRepository.saveAndFlush(configurationAsso);

        // Get all the configurationAssoList
        restConfigurationAssoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationAsso.getId().intValue())))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].objet1").value(hasItem(DEFAULT_OBJET1)))
            .andExpect(jsonPath("$.[*].objet2").value(hasItem(DEFAULT_OBJET2)));
    }

    @Test
    @Transactional
    void getConfigurationAsso() throws Exception {
        // Initialize the database
        configurationAssoRepository.saveAndFlush(configurationAsso);

        // Get the configurationAsso
        restConfigurationAssoMockMvc
            .perform(get(ENTITY_API_URL_ID, configurationAsso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configurationAsso.getId().intValue()))
            .andExpect(jsonPath("$.denomination").value(DEFAULT_DENOMINATION))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.objet1").value(DEFAULT_OBJET1))
            .andExpect(jsonPath("$.objet2").value(DEFAULT_OBJET2));
    }

    @Test
    @Transactional
    void getNonExistingConfigurationAsso() throws Exception {
        // Get the configurationAsso
        restConfigurationAssoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConfigurationAsso() throws Exception {
        // Initialize the database
        configurationAssoRepository.saveAndFlush(configurationAsso);

        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();

        // Update the configurationAsso
        ConfigurationAsso updatedConfigurationAsso = configurationAssoRepository.findById(configurationAsso.getId()).get();
        // Disconnect from session so that the updates on updatedConfigurationAsso are not directly saved in db
        em.detach(updatedConfigurationAsso);
        updatedConfigurationAsso.denomination(UPDATED_DENOMINATION).objet(UPDATED_OBJET).objet1(UPDATED_OBJET1).objet2(UPDATED_OBJET2);
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(updatedConfigurationAsso);

        restConfigurationAssoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configurationAssoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationAsso testConfigurationAsso = configurationAssoList.get(configurationAssoList.size() - 1);
        assertThat(testConfigurationAsso.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testConfigurationAsso.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testConfigurationAsso.getObjet1()).isEqualTo(UPDATED_OBJET1);
        assertThat(testConfigurationAsso.getObjet2()).isEqualTo(UPDATED_OBJET2);
    }

    @Test
    @Transactional
    void putNonExistingConfigurationAsso() throws Exception {
        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();
        configurationAsso.setId(count.incrementAndGet());

        // Create the ConfigurationAsso
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(configurationAsso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationAssoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configurationAssoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfigurationAsso() throws Exception {
        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();
        configurationAsso.setId(count.incrementAndGet());

        // Create the ConfigurationAsso
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(configurationAsso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationAssoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfigurationAsso() throws Exception {
        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();
        configurationAsso.setId(count.incrementAndGet());

        // Create the ConfigurationAsso
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(configurationAsso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationAssoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigurationAssoWithPatch() throws Exception {
        // Initialize the database
        configurationAssoRepository.saveAndFlush(configurationAsso);

        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();

        // Update the configurationAsso using partial update
        ConfigurationAsso partialUpdatedConfigurationAsso = new ConfigurationAsso();
        partialUpdatedConfigurationAsso.setId(configurationAsso.getId());

        partialUpdatedConfigurationAsso.denomination(UPDATED_DENOMINATION).objet(UPDATED_OBJET);

        restConfigurationAssoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigurationAsso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigurationAsso))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationAsso testConfigurationAsso = configurationAssoList.get(configurationAssoList.size() - 1);
        assertThat(testConfigurationAsso.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testConfigurationAsso.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testConfigurationAsso.getObjet1()).isEqualTo(UPDATED_OBJET1);
        assertThat(testConfigurationAsso.getObjet2()).isEqualTo(UPDATED_OBJET2);
    }

    @Test
    @Transactional
    void fullUpdateConfigurationAssoWithPatch() throws Exception {
        // Initialize the database
        configurationAssoRepository.saveAndFlush(configurationAsso);

        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();

        // Update the configurationAsso using partial update
        ConfigurationAsso partialUpdatedConfigurationAsso = new ConfigurationAsso();
        partialUpdatedConfigurationAsso.setId(configurationAsso.getId());

        partialUpdatedConfigurationAsso
            .denomination(UPDATED_DENOMINATION)
            .objet(UPDATED_OBJET)
            .objet1(UPDATED_OBJET1)
            .objet2(UPDATED_OBJET2);

        restConfigurationAssoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigurationAsso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigurationAsso))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationAsso testConfigurationAsso = configurationAssoList.get(configurationAssoList.size() - 1);
        assertThat(testConfigurationAsso.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testConfigurationAsso.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testConfigurationAsso.getObjet2()).isEqualTo(UPDATED_OBJET2);
        assertThat(testConfigurationAsso.getObjet1()).isEqualTo(UPDATED_OBJET1);
    }

    @Test
    @Transactional
    void patchNonExistingConfigurationAsso() throws Exception {
        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();
        configurationAsso.setId(count.incrementAndGet());

        // Create the ConfigurationAsso
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(configurationAsso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationAssoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configurationAssoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfigurationAsso() throws Exception {
        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();
        configurationAsso.setId(count.incrementAndGet());

        // Create the ConfigurationAsso
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(configurationAsso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationAssoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfigurationAsso() throws Exception {
        int databaseSizeBeforeUpdate = configurationAssoRepository.findAll().size();
        configurationAsso.setId(count.incrementAndGet());

        // Create the ConfigurationAsso
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoMapper.toDto(configurationAsso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationAssoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationAssoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigurationAsso in the database
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfigurationAsso() throws Exception {
        // Initialize the database
        configurationAssoRepository.saveAndFlush(configurationAsso);

        int databaseSizeBeforeDelete = configurationAssoRepository.findAll().size();

        // Delete the configurationAsso
        restConfigurationAssoMockMvc
            .perform(delete(ENTITY_API_URL_ID, configurationAsso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigurationAsso> configurationAssoList = configurationAssoRepository.findAll();
        assertThat(configurationAssoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
