package fr.iocean.asso.service;

import fr.iocean.asso.domain.ConfigurationAsso;
import fr.iocean.asso.domain.enumeration.FileEnum;
import fr.iocean.asso.repository.ConfigurationAssoRepository;
import fr.iocean.asso.service.dto.ConfigurationAssoDTO;
import fr.iocean.asso.service.exception.FileAccessException;
import fr.iocean.asso.service.exception.FileNotFoundException;
import fr.iocean.asso.service.mapper.ConfigurationAssoMapper;
import fr.iocean.asso.web.rest.errors.LogoSizeException;
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
 * Service Implementation for managing {@link ConfigurationAsso}.
 */
@Service
@Transactional
public class ConfigurationAssoService {

    private final Logger log = LoggerFactory.getLogger(ConfigurationAssoService.class);

    private final FileService fileService;

    private final ConfigurationAssoRepository configurationAssoRepository;

    private final ConfigurationAssoMapper configurationAssoMapper;

    public ConfigurationAssoService(
        FileService fileService,
        ConfigurationAssoRepository configurationAssoRepository,
        ConfigurationAssoMapper configurationAssoMapper
    ) {
        this.fileService = fileService;
        this.configurationAssoRepository = configurationAssoRepository;
        this.configurationAssoMapper = configurationAssoMapper;
    }

    /**
     * Save a configurationAsso.
     *
     * @param configurationAssoDTO the entity to save.
     * @param signature
     * @return the persisted entity.
     */
    public ConfigurationAssoDTO save(ConfigurationAssoDTO configurationAssoDTO, MultipartFile signature, MultipartFile logo) {
        log.debug("Request to save ConfigurationAsso : {}", configurationAssoDTO);
        ConfigurationAsso configurationAsso = configurationAssoMapper.toEntity(configurationAssoDTO);
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

        if (configurationAssoDTO.isDeleteSignature()) {
            this.deleteSignature();
        }
        if (signature != null) {
            this.deleteSignature();
            this.fileService.saveFiles(FileEnum.SIGNATURE_ASSO, 1l, true, signature);
        }

        if (logo != null) {
            try {
                BufferedImage logoBuffer;
                logoBuffer = ImageIO.read(logo.getInputStream());
                if (logoBuffer.getHeight() > 400 || logoBuffer.getWidth() > 400) {
                    throw new LogoSizeException();
                }
            } catch (IOException e) {
                log.error("IOException error when check logo size");
            }
        }

        if (configurationAssoDTO.isDeleteLogo()) {
            this.deleteLogo();
        }
        if (logo != null) {
            this.deleteLogo();
            this.fileService.saveFiles(FileEnum.LOGO_ASSO, 1l, true, logo);
        }
        configurationAsso = configurationAssoRepository.save(configurationAsso);
        return configurationAssoMapper.toDto(configurationAsso);
    }

    /**
     * Get one configurationAsso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public ConfigurationAssoDTO getConfigurationAsso() {
        log.debug("Request to get ConfigurationAsso");
        Optional<ConfigurationAsso> cdOptional = configurationAssoRepository.findById(1l);
        if (cdOptional.isEmpty()) {
            ConfigurationAssoDTO cd = new ConfigurationAssoDTO();
            cd.setId(1l);
            return cd;
        }
        ConfigurationAssoDTO dto = configurationAssoMapper.toDto(cdOptional.get());
        List<String> fileName = this.fileService.getFilename(dto.getId(), FileEnum.SIGNATURE_ASSO);
        if (fileName != null && !fileName.isEmpty()) {
            dto.setHasSignature(true);
        }
        fileName = this.fileService.getFilename(dto.getId(), FileEnum.LOGO_ASSO);
        if (fileName != null && !fileName.isEmpty()) {
            dto.setHasLogo(true);
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

    public void getLogo(HttpServletResponse response) {
        try {
            List<String> signatureName = fileService.getFilename(1l, FileEnum.LOGO_ASSO);
            if (signatureName.isEmpty()) {
                throw new FileNotFoundException();
            }
            File file = fileService.getFile(FileEnum.LOGO_ASSO, 1l, signatureName.get(0));
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

    private void deleteLogo() {
        List<String> photoFileName = this.fileService.getFilename(1l, FileEnum.LOGO_ASSO);
        photoFileName
            .stream()
            .forEach(fileName -> {
                this.fileService.deleteFile(FileEnum.LOGO_ASSO, 1l, fileName);
            });
    }
}
