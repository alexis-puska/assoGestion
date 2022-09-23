package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.ConfigurationContrat;
import fr.iocean.asso.repository.ConfigurationContratRepository;
import fr.iocean.asso.service.dto.ConfigurationContratDTO;
import fr.iocean.asso.service.mapper.ConfigurationContratMapper;
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
 * Integration tests for the {@link ConfigurationContratResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigurationContratResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/configuration-contrats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfigurationContratRepository configurationContratRepository;

    @Autowired
    private ConfigurationContratMapper configurationContratMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigurationContratMockMvc;

    private ConfigurationContrat configurationContrat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationContrat createEntity(EntityManager em) {
        ConfigurationContrat configurationContrat = new ConfigurationContrat().content(DEFAULT_CONTENT);
        return configurationContrat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationContrat createUpdatedEntity(EntityManager em) {
        ConfigurationContrat configurationContrat = new ConfigurationContrat().content(UPDATED_CONTENT);
        return configurationContrat;
    }

    @BeforeEach
    public void initTest() {
        configurationContrat = createEntity(em);
    }

    @Test
    @Transactional
    void createConfigurationContrat() throws Exception {
        int databaseSizeBeforeCreate = configurationContratRepository.findAll().size();
        // Create the ConfigurationContrat
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(configurationContrat);
        restConfigurationContratMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigurationContrat testConfigurationContrat = configurationContratList.get(configurationContratList.size() - 1);
        assertThat(testConfigurationContrat.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void createConfigurationContratWithExistingId() throws Exception {
        // Create the ConfigurationContrat with an existing ID
        configurationContrat.setId(1L);
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(configurationContrat);

        int databaseSizeBeforeCreate = configurationContratRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationContratMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConfigurationContrats() throws Exception {
        // Initialize the database
        configurationContratRepository.saveAndFlush(configurationContrat);

        // Get all the configurationContratList
        restConfigurationContratMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationContrat.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getConfigurationContrat() throws Exception {
        // Initialize the database
        configurationContratRepository.saveAndFlush(configurationContrat);

        // Get the configurationContrat
        restConfigurationContratMockMvc
            .perform(get(ENTITY_API_URL_ID, configurationContrat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configurationContrat.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingConfigurationContrat() throws Exception {
        // Get the configurationContrat
        restConfigurationContratMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConfigurationContrat() throws Exception {
        // Initialize the database
        configurationContratRepository.saveAndFlush(configurationContrat);

        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();

        // Update the configurationContrat
        ConfigurationContrat updatedConfigurationContrat = configurationContratRepository.findById(configurationContrat.getId()).get();
        // Disconnect from session so that the updates on updatedConfigurationContrat are not directly saved in db
        em.detach(updatedConfigurationContrat);
        updatedConfigurationContrat.content(UPDATED_CONTENT);
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(updatedConfigurationContrat);

        restConfigurationContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configurationContratDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationContrat testConfigurationContrat = configurationContratList.get(configurationContratList.size() - 1);
        assertThat(testConfigurationContrat.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingConfigurationContrat() throws Exception {
        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();
        configurationContrat.setId(count.incrementAndGet());

        // Create the ConfigurationContrat
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(configurationContrat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configurationContratDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfigurationContrat() throws Exception {
        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();
        configurationContrat.setId(count.incrementAndGet());

        // Create the ConfigurationContrat
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(configurationContrat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfigurationContrat() throws Exception {
        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();
        configurationContrat.setId(count.incrementAndGet());

        // Create the ConfigurationContrat
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(configurationContrat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationContratMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigurationContratWithPatch() throws Exception {
        // Initialize the database
        configurationContratRepository.saveAndFlush(configurationContrat);

        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();

        // Update the configurationContrat using partial update
        ConfigurationContrat partialUpdatedConfigurationContrat = new ConfigurationContrat();
        partialUpdatedConfigurationContrat.setId(configurationContrat.getId());

        partialUpdatedConfigurationContrat.content(UPDATED_CONTENT);

        restConfigurationContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigurationContrat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigurationContrat))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationContrat testConfigurationContrat = configurationContratList.get(configurationContratList.size() - 1);
        assertThat(testConfigurationContrat.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateConfigurationContratWithPatch() throws Exception {
        // Initialize the database
        configurationContratRepository.saveAndFlush(configurationContrat);

        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();

        // Update the configurationContrat using partial update
        ConfigurationContrat partialUpdatedConfigurationContrat = new ConfigurationContrat();
        partialUpdatedConfigurationContrat.setId(configurationContrat.getId());

        partialUpdatedConfigurationContrat.content(UPDATED_CONTENT);

        restConfigurationContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigurationContrat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigurationContrat))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationContrat testConfigurationContrat = configurationContratList.get(configurationContratList.size() - 1);
        assertThat(testConfigurationContrat.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingConfigurationContrat() throws Exception {
        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();
        configurationContrat.setId(count.incrementAndGet());

        // Create the ConfigurationContrat
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(configurationContrat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configurationContratDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfigurationContrat() throws Exception {
        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();
        configurationContrat.setId(count.incrementAndGet());

        // Create the ConfigurationContrat
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(configurationContrat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfigurationContrat() throws Exception {
        int databaseSizeBeforeUpdate = configurationContratRepository.findAll().size();
        configurationContrat.setId(count.incrementAndGet());

        // Create the ConfigurationContrat
        ConfigurationContratDTO configurationContratDTO = configurationContratMapper.toDto(configurationContrat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationContratMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configurationContratDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigurationContrat in the database
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfigurationContrat() throws Exception {
        // Initialize the database
        configurationContratRepository.saveAndFlush(configurationContrat);

        int databaseSizeBeforeDelete = configurationContratRepository.findAll().size();

        // Delete the configurationContrat
        restConfigurationContratMockMvc
            .perform(delete(ENTITY_API_URL_ID, configurationContrat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigurationContrat> configurationContratList = configurationContratRepository.findAll();
        assertThat(configurationContratList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
