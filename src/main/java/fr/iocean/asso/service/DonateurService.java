package fr.iocean.asso.service;

import com.itextpdf.text.DocumentException;
import fr.iocean.asso.domain.Donateur;
import fr.iocean.asso.domain.enumeration.FileEnum;
import fr.iocean.asso.repository.DonateurRepository;
import fr.iocean.asso.service.dto.ConfigurationAssoDTO;
import fr.iocean.asso.service.dto.DonateurDTO;
import fr.iocean.asso.service.exception.FileNotFoundException;
import fr.iocean.asso.service.mapper.DonateurMapper;
import fr.iocean.asso.service.pdf.PdfService;
import fr.iocean.asso.service.pdf.object.CerfaDonOrganismeGeneral;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Donateur}.
 */
@Service
@Transactional
public class DonateurService {

    private final Logger log = LoggerFactory.getLogger(DonateurService.class);

    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHEMENT_FILENAME = "attachment;filename=";

    private final ConfigurationAssoService configurationAssoService;

    private final PdfService pdfService;

    private final FileService fileService;

    private final DonateurRepository donateurRepository;

    private final DonateurMapper donateurMapper;

    public DonateurService(
        ConfigurationAssoService configurationAssoService,
        PdfService pdfService,
        FileService fileService,
        DonateurRepository donateurRepository,
        DonateurMapper donateurMapper
    ) {
        this.configurationAssoService = configurationAssoService;
        this.pdfService = pdfService;
        this.fileService = fileService;
        this.donateurRepository = donateurRepository;
        this.donateurMapper = donateurMapper;
    }

    /**
     * Save a donateur.
     *
     * @param donateurDTO the entity to save.
     * @return the persisted entity.
     */
    public DonateurDTO save(DonateurDTO donateurDTO) {
        log.debug("Request to save Donateur : {}", donateurDTO);
        Donateur donateur = donateurMapper.toEntity(donateurDTO);
        donateur = donateurRepository.save(donateur);
        return donateurMapper.toDto(donateur);
    }

    /**
     * Partially update a donateur.
     *
     * @param donateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DonateurDTO> partialUpdate(DonateurDTO donateurDTO) {
        log.debug("Request to partially update Donateur : {}", donateurDTO);

        return donateurRepository
            .findById(donateurDTO.getId())
            .map(existingDonateur -> {
                donateurMapper.partialUpdate(existingDonateur, donateurDTO);

                return existingDonateur;
            })
            .map(donateurRepository::save)
            .map(donateurMapper::toDto);
    }

    /**
     * Get all the donateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DonateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Donateurs");
        return donateurRepository.findAll(pageable).map(donateurMapper::toDto);
    }

    /**
     * Get one donateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DonateurDTO> findOne(Long id) {
        log.debug("Request to get Donateur : {}", id);
        return donateurRepository.findById(id).map(donateurMapper::toDto);
    }

    /**
     * Delete the donateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Donateur : {}", id);
        donateurRepository.deleteById(id);
    }

    /**
     * Fill the cerfa
     *
     * @param donnateurId, id du donnateur
     */
    public void fillCerfa(long id, HttpServletResponse response) {
        Optional<Donateur> donateurOptional = this.donateurRepository.findById(id);
        if (donateurOptional.isPresent()) {
            Donateur donateur = donateurOptional.get();
            CerfaDonOrganismeGeneral cerfa = donateurToCerfaDonOrganismeGeneral(donateur);
            try {
                InputStream inputStream = null;
                List<String> signatureFileName = this.fileService.getFilename(1l, FileEnum.SIGNATURE_ASSO);
                if (signatureFileName != null && !signatureFileName.isEmpty()) {
                    inputStream = new FileInputStream(fileService.getFile(FileEnum.SIGNATURE_ASSO, 1l, signatureFileName.get(0)));
                }
                response.setContentType("application/pdf");
                response.setHeader(
                    CONTENT_DISPOSITION,
                    ATTACHEMENT_FILENAME + "recu_don_" + donateur.getNom() + "_" + donateur.getPrenom() + ".pdf"
                );
                this.pdfService.fillPDF(
                        "pdf/titre_dons_organisme_interet_general.pdf",
                        response.getOutputStream(),
                        cerfa,
                        CerfaDonOrganismeGeneral.class,
                        inputStream,
                        2,
                        370,
                        25
                    );
                inputStream.close();
            } catch (FileNotFoundException e) {
                log.error("FileNotFoundException {}: ", e.getMessage());
            } catch (DocumentException e) {
                log.error("DocumentException : {}", e.getMessage());
            } catch (IOException e) {
                log.error("IOException : {}", e.getMessage());
            }
        }
    }

    private CerfaDonOrganismeGeneral donateurToCerfaDonOrganismeGeneral(Donateur donateur) {
        ConfigurationAssoDTO configDon = configurationAssoService.getConfigurationAsso();

        CerfaDonOrganismeGeneral cerfa = new CerfaDonOrganismeGeneral();
        cerfa.setOeuvreOuOrganismeDinteretGeneral(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
        cerfa.setNumeroOrdreRecu(String.valueOf(donateur.getId()));

        cerfa.setAdresse(configDon.getDenomination());
        cerfa.setNumero(String.valueOf(configDon.getAdresse() != null ? configDon.getAdresse().getNumero() : ""));
        cerfa.setRue(configDon.getAdresse() != null ? configDon.getAdresse().getRue() : "");
        cerfa.setCodePostal(String.valueOf(configDon.getAdresse() != null ? configDon.getAdresse().getCodePostale() : ""));
        cerfa.setCommune(String.valueOf(configDon.getAdresse() != null ? configDon.getAdresse().getVille() : ""));
        cerfa.setObjet1(configDon.getObjet() != null ? configDon.getObjet() : "");
        cerfa.setObjet2(configDon.getObjet1() != null ? configDon.getObjet1() : "");
        cerfa.setObjet3(configDon.getObjet2() != null ? configDon.getObjet2() : "");
        cerfa.setNom(donateur.getNom());
        cerfa.setPrenom(donateur.getPrenom());
        cerfa.setEuros(String.valueOf(donateur.getMontant()));
        cerfa.setSommeEnToutesLettres(donateur.getSommeTouteLettre());
        cerfa.setDate4(donateur.getDateDon().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        switch (donateur.getFormeDon()) {
            case ACTE_AUTHENTIQUE:
                cerfa.setActeAuthentique(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            case ACTE_SOUS_SEIN_PRIVE:
                cerfa.setActeSousSeingPrive(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            case AUTRES:
                cerfa.setAutres(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            case DON_MANUEL:
                cerfa.setDeclarationDeDonManuel(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            default:
                break;
        }
        switch (donateur.getNatureDon()) {
            case AUTRES:
                cerfa.setAutres4(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            case NUMERAIRE:
                cerfa.setNumeraire(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            case TITRE_SOCIETE_COTES:
                cerfa.setTitresDeSocietesCotes(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            default:
                break;
        }
        switch (donateur.getNumeraireDon()) {
            case CB_VIREMENT:
                cerfa.setVirementPrelevementCarteBancaire(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            case CHEQUE:
                cerfa.setCheque(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            case ESPECE:
                cerfa.setRemiseEspece(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
                break;
            default:
                break;
        }
        cerfa.setDuCGI200(CerfaDonOrganismeGeneral.PDF_FIELD_ON);
        cerfa.setAdresse2(donateur.getAdresse().getNumero() + " " + donateur.getAdresse().getRue());
        cerfa.setCodePostal2(donateur.getAdresse().getCodePostale());
        cerfa.setCommune2(donateur.getAdresse().getVille());
        cerfa.setDate5(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return cerfa;
    }
}
