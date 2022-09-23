package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.Chat;
import fr.iocean.asso.domain.enumeration.PoilEnum;
import fr.iocean.asso.domain.enumeration.TypeIdentificationEnum;
import fr.iocean.asso.repository.ChatRepository;
import fr.iocean.asso.service.dto.ChatDTO;
import fr.iocean.asso.service.mapper.ChatMapper;
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
 * Integration tests for the {@link ChatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final TypeIdentificationEnum DEFAULT_TYPE_IDENTIFICATION = TypeIdentificationEnum.PUCE;
    private static final TypeIdentificationEnum UPDATED_TYPE_IDENTIFICATION = TypeIdentificationEnum.TATOUAGE;

    private static final String DEFAULT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ROBE = "AAAAAAAAAA";
    private static final String UPDATED_ROBE = "BBBBBBBBBB";

    private static final PoilEnum DEFAULT_POIL = PoilEnum.SANS;
    private static final PoilEnum UPDATED_POIL = PoilEnum.COURT;

    private static final String ENTITY_API_URL = "/api/chats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatMockMvc;

    private Chat chat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chat createEntity(EntityManager em) {
        Chat chat = new Chat()
            .nom(DEFAULT_NOM)
            .typeIdentification(DEFAULT_TYPE_IDENTIFICATION)
            .identification(DEFAULT_IDENTIFICATION)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .description(DEFAULT_DESCRIPTION)
            .robe(DEFAULT_ROBE)
            .poil(DEFAULT_POIL);
        return chat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chat createUpdatedEntity(EntityManager em) {
        Chat chat = new Chat()
            .nom(UPDATED_NOM)
            .typeIdentification(UPDATED_TYPE_IDENTIFICATION)
            .identification(UPDATED_IDENTIFICATION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .description(UPDATED_DESCRIPTION)
            .robe(UPDATED_ROBE)
            .poil(UPDATED_POIL);
        return chat;
    }

    @BeforeEach
    public void initTest() {
        chat = createEntity(em);
    }

    @Test
    @Transactional
    void createChat() throws Exception {
        int databaseSizeBeforeCreate = chatRepository.findAll().size();
        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);
        restChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isCreated());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeCreate + 1);
        Chat testChat = chatList.get(chatList.size() - 1);
        assertThat(testChat.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testChat.getTypeIdentification()).isEqualTo(DEFAULT_TYPE_IDENTIFICATION);
        assertThat(testChat.getIdentification()).isEqualTo(DEFAULT_IDENTIFICATION);
        assertThat(testChat.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testChat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChat.getRobe()).isEqualTo(DEFAULT_ROBE);
        assertThat(testChat.getPoil()).isEqualTo(DEFAULT_POIL);
    }

    @Test
    @Transactional
    void createChatWithExistingId() throws Exception {
        // Create the Chat with an existing ID
        chat.setId(1L);
        ChatDTO chatDTO = chatMapper.toDto(chat);

        int databaseSizeBeforeCreate = chatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRepository.findAll().size();
        // set the field null
        chat.setNom(null);

        // Create the Chat, which fails.
        ChatDTO chatDTO = chatMapper.toDto(chat);

        restChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isBadRequest());

        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRepository.findAll().size();
        // set the field null
        chat.setDateNaissance(null);

        // Create the Chat, which fails.
        ChatDTO chatDTO = chatMapper.toDto(chat);

        restChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isBadRequest());

        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRobeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRepository.findAll().size();
        // set the field null
        chat.setRobe(null);

        // Create the Chat, which fails.
        ChatDTO chatDTO = chatMapper.toDto(chat);

        restChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isBadRequest());

        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPoilIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRepository.findAll().size();
        // set the field null
        chat.setPoil(null);

        // Create the Chat, which fails.
        ChatDTO chatDTO = chatMapper.toDto(chat);

        restChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isBadRequest());

        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChats() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        // Get all the chatList
        restChatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chat.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].typeIdentification").value(hasItem(DEFAULT_TYPE_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].robe").value(hasItem(DEFAULT_ROBE)))
            .andExpect(jsonPath("$.[*].poil").value(hasItem(DEFAULT_POIL.toString())));
    }

    @Test
    @Transactional
    void getChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        // Get the chat
        restChatMockMvc
            .perform(get(ENTITY_API_URL_ID, chat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chat.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.typeIdentification").value(DEFAULT_TYPE_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.identification").value(DEFAULT_IDENTIFICATION))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.robe").value(DEFAULT_ROBE))
            .andExpect(jsonPath("$.poil").value(DEFAULT_POIL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChat() throws Exception {
        // Get the chat
        restChatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        int databaseSizeBeforeUpdate = chatRepository.findAll().size();

        // Update the chat
        Chat updatedChat = chatRepository.findById(chat.getId()).get();
        // Disconnect from session so that the updates on updatedChat are not directly saved in db
        em.detach(updatedChat);
        updatedChat
            .nom(UPDATED_NOM)
            .typeIdentification(UPDATED_TYPE_IDENTIFICATION)
            .identification(UPDATED_IDENTIFICATION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .description(UPDATED_DESCRIPTION)
            .robe(UPDATED_ROBE)
            .poil(UPDATED_POIL);
        ChatDTO chatDTO = chatMapper.toDto(updatedChat);

        restChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
        Chat testChat = chatList.get(chatList.size() - 1);
        assertThat(testChat.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testChat.getTypeIdentification()).isEqualTo(UPDATED_TYPE_IDENTIFICATION);
        assertThat(testChat.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testChat.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testChat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChat.getRobe()).isEqualTo(UPDATED_ROBE);
        assertThat(testChat.getPoil()).isEqualTo(UPDATED_POIL);
    }

    @Test
    @Transactional
    void putNonExistingChat() throws Exception {
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();
        chat.setId(count.incrementAndGet());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChat() throws Exception {
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();
        chat.setId(count.incrementAndGet());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChat() throws Exception {
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();
        chat.setId(count.incrementAndGet());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatWithPatch() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        int databaseSizeBeforeUpdate = chatRepository.findAll().size();

        // Update the chat using partial update
        Chat partialUpdatedChat = new Chat();
        partialUpdatedChat.setId(chat.getId());

        partialUpdatedChat.nom(UPDATED_NOM).identification(UPDATED_IDENTIFICATION).dateNaissance(UPDATED_DATE_NAISSANCE);

        restChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChat))
            )
            .andExpect(status().isOk());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
        Chat testChat = chatList.get(chatList.size() - 1);
        assertThat(testChat.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testChat.getTypeIdentification()).isEqualTo(DEFAULT_TYPE_IDENTIFICATION);
        assertThat(testChat.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testChat.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testChat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChat.getRobe()).isEqualTo(DEFAULT_ROBE);
        assertThat(testChat.getPoil()).isEqualTo(DEFAULT_POIL);
    }

    @Test
    @Transactional
    void fullUpdateChatWithPatch() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        int databaseSizeBeforeUpdate = chatRepository.findAll().size();

        // Update the chat using partial update
        Chat partialUpdatedChat = new Chat();
        partialUpdatedChat.setId(chat.getId());

        partialUpdatedChat
            .nom(UPDATED_NOM)
            .typeIdentification(UPDATED_TYPE_IDENTIFICATION)
            .identification(UPDATED_IDENTIFICATION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .description(UPDATED_DESCRIPTION)
            .robe(UPDATED_ROBE)
            .poil(UPDATED_POIL);

        restChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChat))
            )
            .andExpect(status().isOk());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
        Chat testChat = chatList.get(chatList.size() - 1);
        assertThat(testChat.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testChat.getTypeIdentification()).isEqualTo(UPDATED_TYPE_IDENTIFICATION);
        assertThat(testChat.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testChat.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testChat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChat.getRobe()).isEqualTo(UPDATED_ROBE);
        assertThat(testChat.getPoil()).isEqualTo(UPDATED_POIL);
    }

    @Test
    @Transactional
    void patchNonExistingChat() throws Exception {
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();
        chat.setId(count.incrementAndGet());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChat() throws Exception {
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();
        chat.setId(count.incrementAndGet());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChat() throws Exception {
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();
        chat.setId(count.incrementAndGet());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        int databaseSizeBeforeDelete = chatRepository.findAll().size();

        // Delete the chat
        restChatMockMvc
            .perform(delete(ENTITY_API_URL_ID, chat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
