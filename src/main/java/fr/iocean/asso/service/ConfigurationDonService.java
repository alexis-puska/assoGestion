package fr.iocean.asso.service;

import fr.iocean.asso.domain.ConfigurationDon;
import fr.iocean.asso.domain.enumeration.FileEnum;
import fr.iocean.asso.repository.ConfigurationDonRepository;
import fr.iocean.asso.service.dto.ConfigurationDonDTO;
import fr.iocean.asso.service.exception.FileAccessException;
import fr.iocean.asso.service.exception.FileNotFoundException;
import fr.iocean.asso.service.mapper.ConfigurationDonMapper;
import fr.iocean.asso.web.rest.errors.SignatureSizeException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link ConfigurationDon}.
 */
@Service
@Transactional
public class ConfigurationDonService {

    private final Logger log = LoggerFactory.getLogger(ConfigurationDonService.class);

    private final FileService fileService;

    private final ConfigurationDonRepository configurationDonRepository;

    private final ConfigurationDonMapper configurationDonMapper;

    public ConfigurationDonService(
        FileService fileService,
        ConfigurationDonRepository configurationDonRepository,
        ConfigurationDonMapper configurationDonMapper
    ) {
        this.fileService = fileService;
        this.configurationDonRepository = configurationDonRepository;
        this.configurationDonMapper = configurationDonMapper;
    }

    /**
     * Save a configurationDon.
     *
     * @param configurationDonDTO the entity to save.
     * @param signature
     * @return the persisted entity.
     */
    public ConfigurationDonDTO save(ConfigurationDonDTO configurationDonDTO, MultipartFile signature) {
        log.debug("Request to save ConfigurationDon : {}", configurationDonDTO);
        ConfigurationDon configurationDon = configurationDonMapper.toEntity(configurationDonDTO);
        if (signature != null) {
            try {
                BufferedImage signatureBuffer;
                signatureBuffer = ImageIO.read(signature.getInputStream());
                if (signatureBuffer.getHeight() > 75 || signatureBuffer.getWidth() > 150) {
                    throw new SignatureSizeException();
                }
            } catch (IOException e) {
                log.error("IOException error when check signature size");
            }
        }

        if (configurationDonDTO.isDeleteSignature()) {
            this.deleteSignature();
        }
        if (signature != null) {
            this.deleteSignature();
            this.fileService.saveFiles(FileEnum.SIGNATURE_ASSO, 1l, true, signature);
        }
        configurationDon = configurationDonRepository.save(configurationDon);
        return configurationDonMapper.toDto(configurationDon);
    }

    /**
     * Get one configurationDon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public ConfigurationDonDTO getConfigurationDon() {
        log.debug("Request to get ConfigurationDon");
        Optional<ConfigurationDon> cdOptional = configurationDonRepository.findById(1l);
        if (cdOptional.isEmpty()) {
            ConfigurationDonDTO cd = new ConfigurationDonDTO();
            cd.setId(1l);
            return cd;
        }
        ConfigurationDonDTO dto = configurationDonMapper.toDto(cdOptional.get());
        List<String> fileName = this.fileService.getFilename(dto.getId(), FileEnum.SIGNATURE_ASSO);
        if (fileName != null && !fileName.isEmpty()) {
            dto.setHasSignature(true);
        }
        return dto;
    }

    public void getSignature(HttpServletResponse response) {
        try {
            List<String> signatureName = fileService.getFilename(1l, FileEnum.SIGNATURE_ASSO);
            if (signatureName.isEmpty()) {
                throw new FileNotFoundException();
            }
            File file = fileService.getFile(FileEnum.SIGNATURE_ASSO, 1l, signatureName.get(0));
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

    private void deleteSignature() {
        List<String> photoFileName = this.fileService.getFilename(1l, FileEnum.SIGNATURE_ASSO);
        photoFileName
            .stream()
            .forEach(fileName -> {
                this.fileService.deleteFile(FileEnum.SIGNATURE_ASSO, 1l, fileName);
            });
    }
}
