package fr.iocean.asso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.iocean.asso.IntegrationTest;
import fr.iocean.asso.domain.Donateur;
import fr.iocean.asso.domain.enumeration.FormeDonEnum;
import fr.iocean.asso.domain.enumeration.NatureDon;
import fr.iocean.asso.domain.enumeration.NumeraireDonEnum;
import fr.iocean.asso.repository.DonateurRepository;
import fr.iocean.asso.service.dto.DonateurDTO;
import fr.iocean.asso.service.mapper.DonateurMapper;
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
 * Integration tests for the {@link DonateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonateurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final String DEFAULT_SOMME_TOUTE_LETTRE = "AAAAAAAAAA";
    private static final String UPDATED_SOMME_TOUTE_LETTRE = "BBBBBBBBBB";

    private static final FormeDonEnum DEFAULT_FORME_DON = FormeDonEnum.ACTE_AUTHENTIQUE;
    private static final FormeDonEnum UPDATED_FORME_DON = FormeDonEnum.ACTE_SOUS_SEIN_PRIVE;

    private static final NatureDon DEFAULT_NATURE_DON = NatureDon.NUMERAIRE;
    private static final NatureDon UPDATED_NATURE_DON = NatureDon.TITRE_SOCIETE_COTES;

    private static final NumeraireDonEnum DEFAULT_NUMERAIRE_DON = NumeraireDonEnum.ESPECE;
    private static final NumeraireDonEnum UPDATED_NUMERAIRE_DON = NumeraireDonEnum.CHEQUE;

    private static final String ENTITY_API_URL = "/api/donateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DonateurRepository donateurRepository;

    @Autowired
    private DonateurMapper donateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonateurMockMvc;

    private Donateur donateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donateur createEntity(EntityManager em) {
        Donateur donateur = new Donateur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .montant(DEFAULT_MONTANT)
            .sommeTouteLettre(DEFAULT_SOMME_TOUTE_LETTRE)
            .formeDon(DEFAULT_FORME_DON)
            .natureDon(DEFAULT_NATURE_DON)
            .numeraireDon(DEFAULT_NUMERAIRE_DON);
        return donateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donateur createUpdatedEntity(EntityManager em) {
        Donateur donateur = new Donateur()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .montant(UPDATED_MONTANT)
            .sommeTouteLettre(UPDATED_SOMME_TOUTE_LETTRE)
            .formeDon(UPDATED_FORME_DON)
            .natureDon(UPDATED_NATURE_DON)
            .numeraireDon(UPDATED_NUMERAIRE_DON);
        return donateur;
    }

    @BeforeEach
    public void initTest() {
        donateur = createEntity(em);
    }

    @Test
    @Transactional
    void createDonateur() throws Exception {
        int databaseSizeBeforeCreate = donateurRepository.findAll().size();
        // Create the Donateur
        DonateurDTO donateurDTO = donateurMapper.toDto(donateur);
        restDonateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donateurDTO)))
            .andExpect(status().isCreated());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeCreate + 1);
        Donateur testDonateur = donateurList.get(donateurList.size() - 1);
        assertThat(testDonateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDonateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDonateur.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testDonateur.getSommeTouteLettre()).isEqualTo(DEFAULT_SOMME_TOUTE_LETTRE);
        assertThat(testDonateur.getFormeDon()).isEqualTo(DEFAULT_FORME_DON);
        assertThat(testDonateur.getNatureDon()).isEqualTo(DEFAULT_NATURE_DON);
        assertThat(testDonateur.getNumeraireDon()).isEqualTo(DEFAULT_NUMERAIRE_DON);
    }

    @Test
    @Transactional
    void createDonateurWithExistingId() throws Exception {
        // Create the Donateur with an existing ID
        donateur.setId(1L);
        DonateurDTO donateurDTO = donateurMapper.toDto(donateur);

        int databaseSizeBeforeCreate = donateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDonateurs() throws Exception {
        // Initialize the database
        donateurRepository.saveAndFlush(donateur);

        // Get all the donateurList
        restDonateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].sommeTouteLettre").value(hasItem(DEFAULT_SOMME_TOUTE_LETTRE)))
            .andExpect(jsonPath("$.[*].formeDon").value(hasItem(DEFAULT_FORME_DON.toString())))
            .andExpect(jsonPath("$.[*].natureDon").value(hasItem(DEFAULT_NATURE_DON.toString())))
            .andExpect(jsonPath("$.[*].numeraireDon").value(hasItem(DEFAULT_NUMERAIRE_DON.toString())));
    }

    @Test
    @Transactional
    void getDonateur() throws Exception {
        // Initialize the database
        donateurRepository.saveAndFlush(donateur);

        // Get the donateur
        restDonateurMockMvc
            .perform(get(ENTITY_API_URL_ID, donateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.sommeTouteLettre").value(DEFAULT_SOMME_TOUTE_LETTRE))
            .andExpect(jsonPath("$.formeDon").value(DEFAULT_FORME_DON.toString()))
            .andExpect(jsonPath("$.natureDon").value(DEFAULT_NATURE_DON.toString()))
            .andExpect(jsonPath("$.numeraireDon").value(DEFAULT_NUMERAIRE_DON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDonateur() throws Exception {
        // Get the donateur
        restDonateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDonateur() throws Exception {
        // Initialize the database
        donateurRepository.saveAndFlush(donateur);

        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();

        // Update the donateur
        Donateur updatedDonateur = donateurRepository.findById(donateur.getId()).get();
        // Disconnect from session so that the updates on updatedDonateur are not directly saved in db
        em.detach(updatedDonateur);
        updatedDonateur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .montant(UPDATED_MONTANT)
            .sommeTouteLettre(UPDATED_SOMME_TOUTE_LETTRE)
            .formeDon(UPDATED_FORME_DON)
            .natureDon(UPDATED_NATURE_DON)
            .numeraireDon(UPDATED_NUMERAIRE_DON);
        DonateurDTO donateurDTO = donateurMapper.toDto(updatedDonateur);

        restDonateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
        Donateur testDonateur = donateurList.get(donateurList.size() - 1);
        assertThat(testDonateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDonateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDonateur.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDonateur.getSommeTouteLettre()).isEqualTo(UPDATED_SOMME_TOUTE_LETTRE);
        assertThat(testDonateur.getFormeDon()).isEqualTo(UPDATED_FORME_DON);
        assertThat(testDonateur.getNatureDon()).isEqualTo(UPDATED_NATURE_DON);
        assertThat(testDonateur.getNumeraireDon()).isEqualTo(UPDATED_NUMERAIRE_DON);
    }

    @Test
    @Transactional
    void putNonExistingDonateur() throws Exception {
        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();
        donateur.setId(count.incrementAndGet());

        // Create the Donateur
        DonateurDTO donateurDTO = donateurMapper.toDto(donateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDonateur() throws Exception {
        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();
        donateur.setId(count.incrementAndGet());

        // Create the Donateur
        DonateurDTO donateurDTO = donateurMapper.toDto(donateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDonateur() throws Exception {
        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();
        donateur.setId(count.incrementAndGet());

        // Create the Donateur
        DonateurDTO donateurDTO = donateurMapper.toDto(donateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDonateurWithPatch() throws Exception {
        // Initialize the database
        donateurRepository.saveAndFlush(donateur);

        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();

        // Update the donateur using partial update
        Donateur partialUpdatedDonateur = new Donateur();
        partialUpdatedDonateur.setId(donateur.getId());

        partialUpdatedDonateur
            .nom(UPDATED_NOM)
            .sommeTouteLettre(UPDATED_SOMME_TOUTE_LETTRE)
            .formeDon(UPDATED_FORME_DON)
            .natureDon(UPDATED_NATURE_DON)
            .numeraireDon(UPDATED_NUMERAIRE_DON);

        restDonateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonateur))
            )
            .andExpect(status().isOk());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
        Donateur testDonateur = donateurList.get(donateurList.size() - 1);
        assertThat(testDonateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDonateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDonateur.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testDonateur.getSommeTouteLettre()).isEqualTo(UPDATED_SOMME_TOUTE_LETTRE);
        assertThat(testDonateur.getFormeDon()).isEqualTo(UPDATED_FORME_DON);
        assertThat(testDonateur.getNatureDon()).isEqualTo(UPDATED_NATURE_DON);
        assertThat(testDonateur.getNumeraireDon()).isEqualTo(UPDATED_NUMERAIRE_DON);
    }

    @Test
    @Transactional
    void fullUpdateDonateurWithPatch() throws Exception {
        // Initialize the database
        donateurRepository.saveAndFlush(donateur);

        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();

        // Update the donateur using partial update
        Donateur partialUpdatedDonateur = new Donateur();
        partialUpdatedDonateur.setId(donateur.getId());

        partialUpdatedDonateur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .montant(UPDATED_MONTANT)
            .sommeTouteLettre(UPDATED_SOMME_TOUTE_LETTRE)
            .formeDon(UPDATED_FORME_DON)
            .natureDon(UPDATED_NATURE_DON)
            .numeraireDon(UPDATED_NUMERAIRE_DON);

        restDonateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonateur))
            )
            .andExpect(status().isOk());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
        Donateur testDonateur = donateurList.get(donateurList.size() - 1);
        assertThat(testDonateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDonateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDonateur.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDonateur.getSommeTouteLettre()).isEqualTo(UPDATED_SOMME_TOUTE_LETTRE);
        assertThat(testDonateur.getFormeDon()).isEqualTo(UPDATED_FORME_DON);
        assertThat(testDonateur.getNatureDon()).isEqualTo(UPDATED_NATURE_DON);
        assertThat(testDonateur.getNumeraireDon()).isEqualTo(UPDATED_NUMERAIRE_DON);
    }

    @Test
    @Transactional
    void patchNonExistingDonateur() throws Exception {
        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();
        donateur.setId(count.incrementAndGet());

        // Create the Donateur
        DonateurDTO donateurDTO = donateurMapper.toDto(donateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDonateur() throws Exception {
        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();
        donateur.setId(count.incrementAndGet());

        // Create the Donateur
        DonateurDTO donateurDTO = donateurMapper.toDto(donateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDonateur() throws Exception {
        int databaseSizeBeforeUpdate = donateurRepository.findAll().size();
        donateur.setId(count.incrementAndGet());

        // Create the Donateur
        DonateurDTO donateurDTO = donateurMapper.toDto(donateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonateurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(donateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donateur in the database
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDonateur() throws Exception {
        // Initialize the database
        donateurRepository.saveAndFlush(donateur);

        int databaseSizeBeforeDelete = donateurRepository.findAll().size();

        // Delete the donateur
        restDonateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, donateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Donateur> donateurList = donateurRepository.findAll();
        assertThat(donateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
