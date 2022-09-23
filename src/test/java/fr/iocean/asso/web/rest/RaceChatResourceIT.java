package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.RaceChat;
import fr.iocean.asso.repository.RaceChatRepository;
import fr.iocean.asso.service.dto.RaceChatDTO;
import fr.iocean.asso.service.mapper.RaceChatMapper;
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
 * Integration tests for the {@link RaceChatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RaceChatResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/race-chats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RaceChatRepository raceChatRepository;

    @Autowired
    private RaceChatMapper raceChatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRaceChatMockMvc;

    private RaceChat raceChat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaceChat createEntity(EntityManager em) {
        RaceChat raceChat = new RaceChat().libelle(DEFAULT_LIBELLE);
        return raceChat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaceChat createUpdatedEntity(EntityManager em) {
        RaceChat raceChat = new RaceChat().libelle(UPDATED_LIBELLE);
        return raceChat;
    }

    @BeforeEach
    public void initTest() {
        raceChat = createEntity(em);
    }

    @Test
    @Transactional
    void createRaceChat() throws Exception {
        int databaseSizeBeforeCreate = raceChatRepository.findAll().size();
        // Create the RaceChat
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(raceChat);
        restRaceChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raceChatDTO)))
            .andExpect(status().isCreated());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeCreate + 1);
        RaceChat testRaceChat = raceChatList.get(raceChatList.size() - 1);
        assertThat(testRaceChat.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createRaceChatWithExistingId() throws Exception {
        // Create the RaceChat with an existing ID
        raceChat.setId(1L);
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(raceChat);

        int databaseSizeBeforeCreate = raceChatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaceChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raceChatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRaceChats() throws Exception {
        // Initialize the database
        raceChatRepository.saveAndFlush(raceChat);

        // Get all the raceChatList
        restRaceChatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raceChat.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getRaceChat() throws Exception {
        // Initialize the database
        raceChatRepository.saveAndFlush(raceChat);

        // Get the raceChat
        restRaceChatMockMvc
            .perform(get(ENTITY_API_URL_ID, raceChat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(raceChat.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingRaceChat() throws Exception {
        // Get the raceChat
        restRaceChatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRaceChat() throws Exception {
        // Initialize the database
        raceChatRepository.saveAndFlush(raceChat);

        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();

        // Update the raceChat
        RaceChat updatedRaceChat = raceChatRepository.findById(raceChat.getId()).get();
        // Disconnect from session so that the updates on updatedRaceChat are not directly saved in db
        em.detach(updatedRaceChat);
        updatedRaceChat.libelle(UPDATED_LIBELLE);
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(updatedRaceChat);

        restRaceChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, raceChatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(raceChatDTO))
            )
            .andExpect(status().isOk());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
        RaceChat testRaceChat = raceChatList.get(raceChatList.size() - 1);
        assertThat(testRaceChat.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingRaceChat() throws Exception {
        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();
        raceChat.setId(count.incrementAndGet());

        // Create the RaceChat
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(raceChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaceChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, raceChatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(raceChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRaceChat() throws Exception {
        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();
        raceChat.setId(count.incrementAndGet());

        // Create the RaceChat
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(raceChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaceChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(raceChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRaceChat() throws Exception {
        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();
        raceChat.setId(count.incrementAndGet());

        // Create the RaceChat
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(raceChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaceChatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raceChatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRaceChatWithPatch() throws Exception {
        // Initialize the database
        raceChatRepository.saveAndFlush(raceChat);

        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();

        // Update the raceChat using partial update
        RaceChat partialUpdatedRaceChat = new RaceChat();
        partialUpdatedRaceChat.setId(raceChat.getId());

        restRaceChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaceChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRaceChat))
            )
            .andExpect(status().isOk());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
        RaceChat testRaceChat = raceChatList.get(raceChatList.size() - 1);
        assertThat(testRaceChat.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateRaceChatWithPatch() throws Exception {
        // Initialize the database
        raceChatRepository.saveAndFlush(raceChat);

        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();

        // Update the raceChat using partial update
        RaceChat partialUpdatedRaceChat = new RaceChat();
        partialUpdatedRaceChat.setId(raceChat.getId());

        partialUpdatedRaceChat.libelle(UPDATED_LIBELLE);

        restRaceChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaceChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRaceChat))
            )
            .andExpect(status().isOk());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
        RaceChat testRaceChat = raceChatList.get(raceChatList.size() - 1);
        assertThat(testRaceChat.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingRaceChat() throws Exception {
        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();
        raceChat.setId(count.incrementAndGet());

        // Create the RaceChat
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(raceChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaceChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, raceChatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(raceChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRaceChat() throws Exception {
        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();
        raceChat.setId(count.incrementAndGet());

        // Create the RaceChat
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(raceChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaceChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(raceChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRaceChat() throws Exception {
        int databaseSizeBeforeUpdate = raceChatRepository.findAll().size();
        raceChat.setId(count.incrementAndGet());

        // Create the RaceChat
        RaceChatDTO raceChatDTO = raceChatMapper.toDto(raceChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaceChatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(raceChatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RaceChat in the database
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRaceChat() throws Exception {
        // Initialize the database
        raceChatRepository.saveAndFlush(raceChat);

        int databaseSizeBeforeDelete = raceChatRepository.findAll().size();

        // Delete the raceChat
        restRaceChatMockMvc
            .perform(delete(ENTITY_API_URL_ID, raceChat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RaceChat> raceChatList = raceChatRepository.findAll();
        assertThat(raceChatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
