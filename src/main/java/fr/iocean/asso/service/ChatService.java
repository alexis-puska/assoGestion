package fr.iocean.asso.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import fr.iocean.asso.domain.Chat;
import fr.iocean.asso.domain.enumeration.FileEnum;
import fr.iocean.asso.repository.ChatRepository;
import fr.iocean.asso.service.dto.ChatDTO;
import fr.iocean.asso.service.dto.ConfigurationAssoDTO;
import fr.iocean.asso.service.exception.FileAccessException;
import fr.iocean.asso.service.exception.FileNotFoundException;
import fr.iocean.asso.service.mapper.ChatMapper;
import fr.iocean.asso.service.pdf.PdfFooter;
import fr.iocean.asso.service.pdf.PdfHeader;
import fr.iocean.asso.service.pdf.PdfService;
import fr.iocean.asso.web.rest.errors.ChatNotFoundException;
import fr.iocean.asso.web.rest.errors.ContratNotExistsException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

/**
 * Service Implementation for managing {@link Chat}.
 */
@Service
@Transactional
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHEMENT_FILENAME = "attachment;filename=";
    private static final String CONTRAT_TEMPLATE = "pdf/contrat";
    private static final String CONTRAT_CSS = "templates/thymleaf/css/contrat.css";

    private final FileService fileService;

    private final PdfService pdfService;

    private final ConfigurationAssoService configurationAssoService;

    private final ChatRepository chatRepository;

    private final ChatMapper chatMapper;

    public ChatService(
        FileService fileService,
        PdfService pdfService,
        ConfigurationAssoService configurationAssoService,
        ChatRepository chatRepository,
        ChatMapper chatMapper
    ) {
        this.fileService = fileService;
        this.pdfService = pdfService;
        this.configurationAssoService = configurationAssoService;
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
    }

    /**
     * Save a chat.
     *
     * @param chatDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatDTO save(ChatDTO chatDTO, MultipartFile photo) {
        log.debug("Request to save Chat : {}", chatDTO);
        Chat chat = chatMapper.toEntity(chatDTO);
        chat = chatRepository.save(chat);
        if (chatDTO.isDeletePhoto()) {
            this.deletePhoto(chatDTO.getId());
        }
        if (photo != null) {
            this.deletePhoto(chat.getId());
            this.fileService.saveFiles(FileEnum.PHOTO_CHAT, chat.getId(), true, photo);
        }
        return chatMapper.toDto(chat);
    }

    /**
     * Partially update a chat.
     *
     * @param chatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatDTO> partialUpdate(ChatDTO chatDTO) {
        log.debug("Request to partially update Chat : {}", chatDTO);

        return chatRepository
            .findById(chatDTO.getId())
            .map(existingChat -> {
                chatMapper.partialUpdate(existingChat, chatDTO);

                return existingChat;
            })
            .map(chatRepository::save)
            .map(chatMapper::toDto);
    }

    /**
     * Get all the chats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Chats");
        return chatRepository
            .findAll(pageable)
            .map(chatMapper::toDto)
            .map(dto -> {
                List<String> fileName = this.fileService.getFilename(dto.getId(), FileEnum.PHOTO_CHAT);
                if (fileName != null && !fileName.isEmpty()) {
                    dto.setHasPhoto(true);
                }
                return dto;
            });
    }

    /**
     * Get one chat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatDTO> findOne(Long id) {
        log.debug("Request to get Chat : {}", id);
        return chatRepository
            .findById(id)
            .map(chatMapper::toDto)
            .map(dto -> {
                List<String> fileName = this.fileService.getFilename(dto.getId(), FileEnum.PHOTO_CHAT);
                if (fileName != null && !fileName.isEmpty()) {
                    dto.setHasPhoto(true);
                }
                return dto;
            });
    }

    /**
     * Delete the chat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Chat : {}", id);
        chatRepository.deleteById(id);
        this.fileService.deleteFolder(FileEnum.PHOTO_CHAT, id, true);
    }

    public void getPhoto(HttpServletResponse response, long id) {
        try {
            List<String> photoName = fileService.getFilename(id, FileEnum.PHOTO_CHAT);
            if (photoName.isEmpty()) {
                throw new FileNotFoundException();
            }
            File file = fileService.getFile(FileEnum.PHOTO_CHAT, id, photoName.get(0));
            if (file != null) {
                response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
                try (FileInputStream fs = new FileInputStream(file)) {
                    FileCopyUtils.copy(fs, response.getOutputStream());
                }
                return;
            }
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    private void deletePhoto(long id) {
        List<String> photoFileName = this.fileService.getFilename(id, FileEnum.PHOTO_CHAT);
        photoFileName
            .stream()
            .forEach(fileName -> {
                this.fileService.deleteFile(FileEnum.PHOTO_CHAT, id, fileName);
            });
    }

    public void generateContrat(HttpServletResponse response, long id) {
        ConfigurationAssoDTO configDTO = configurationAssoService.getConfigurationAsso();
        Optional<Chat> chatOptional = this.chatRepository.findById(id);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            if (chat.getContrat() == null) {
                throw new ContratNotExistsException();
            }
            ChatDTO chatDTO = this.chatMapper.toDto(chat);

            response.setContentType("application/pdf");
            response.setHeader(CONTENT_DISPOSITION, ATTACHEMENT_FILENAME + "contrat_test.pdf");

            Locale locale = Locale.forLanguageTag("fr");
            Context context = new Context(locale);

            context.setVariable("chat", chatDTO);
            context.setVariable("configAsso", configDTO);

            String footer = String.format(
                "%s\n%s",
                configDTO.getDenomination() != null ? configDTO.getDenomination() : "",
                configDTO.getSiret() != null ? configDTO.getSiret() : ""
            );
            // Chargement images + logo header
            try {
                byte[] logoByteArray = null;
                List<String> signatureFileName = this.fileService.getFilename(1l, FileEnum.SIGNATURE_ASSO);
                if (signatureFileName != null && !signatureFileName.isEmpty()) {
                    String signature = fileService.getFileBase64(FileEnum.SIGNATURE_ASSO, 1l, signatureFileName.get(0));
                    if (signature != null) {
                        context.setVariable("signature", "data:image/png;base64," + signature);
                    }
                }
                List<String> logoFileName = this.fileService.getFilename(1l, FileEnum.LOGO_ASSO);
                if (logoFileName != null && !logoFileName.isEmpty()) {
                    String logo = fileService.getFileBase64(FileEnum.LOGO_ASSO, 1l, logoFileName.get(0));
                    if (logo != null) {
                        context.setVariable("logo", "data:image/png;base64," + logo);
                        FileInputStream fis = new FileInputStream(fileService.getFile(FileEnum.LOGO_ASSO, 1l, logoFileName.get(0)));
                        logoByteArray = IOUtils.toByteArray(fis);
                        fis.close();
                    }
                }
                String checkbox = this.fileService.getClasspathFileBase64("classpath:pdf/checkbox.png");
                if (checkbox != null) {
                    context.setVariable("checkbox", "data:image/png;base64," + checkbox);
                }
                String checkboxChecked = this.fileService.getClasspathFileBase64("classpath:pdf/checkbox_checked.png");
                if (checkboxChecked != null) {
                    context.setVariable("checkbox_checked", "data:image/png;base64," + checkboxChecked);
                }
                this.pdfService.printPDF(
                        response.getOutputStream(),
                        PageSize.A4,
                        context,
                        CONTRAT_TEMPLATE,
                        this.pdfService.getCssFile(CONTRAT_CSS),
                        new PdfFooter(footer, 8, true, false),
                        logoByteArray != null ? new PdfHeader(false, Image.getInstance(logoByteArray)) : null,
                        true
                    );
            } catch (FileNotFoundException e) {
                log.error("file not found : {}", e.getMessage());
            } catch (IOException e) {
                log.error("IO Exception : {}", e.getMessage());
            } catch (DocumentException e) {
                log.error("Document Exception : {}", e.getMessage());
            }
        } else {
            throw new ChatNotFoundException();
        }
    }
}
