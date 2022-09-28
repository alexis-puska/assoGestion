package fr.iocean.asso.service;

import fr.iocean.asso.domain.Chat;
import fr.iocean.asso.domain.enumeration.FileEnum;
import fr.iocean.asso.repository.ChatRepository;
import fr.iocean.asso.service.dto.ChatDTO;
import fr.iocean.asso.service.exception.FileAccessException;
import fr.iocean.asso.service.exception.FileNotFoundException;
import fr.iocean.asso.service.mapper.ChatMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Chat}.
 */
@Service
@Transactional
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final FileService fileService;

    private final ChatRepository chatRepository;

    private final ChatMapper chatMapper;

    public ChatService(FileService fileService, ChatRepository chatRepository, ChatMapper chatMapper) {
        this.fileService = fileService;
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
            this.deletePhoto(chatDTO.getId());
            this.fileService.saveFiles(FileEnum.PHOTO_CHAT, chatDTO.getId(), true, photo);
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
}
