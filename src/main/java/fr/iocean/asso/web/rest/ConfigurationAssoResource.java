package fr.iocean.asso.web.rest;

import fr.iocean.asso.service.ConfigurationAssoService;
import fr.iocean.asso.service.dto.ConfigurationAssoDTO;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link fr.iocean.asso.domain.ConfigurationAsso}.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationAssoResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationAssoResource.class);

    private static final String ENTITY_NAME = "configurationAsso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationAssoService configurationAssoService;

    public ConfigurationAssoResource(ConfigurationAssoService configurationAssoService) {
        this.configurationAssoService = configurationAssoService;
    }

    @PutMapping("/configuration-assos")
    public ResponseEntity<ConfigurationAssoDTO> updateConfigurationAsso(
        @RequestPart(name = "configurationAsso") ConfigurationAssoDTO configurationAssoDTO,
        @RequestPart(name = "signature", required = false) MultipartFile signature
    ) throws URISyntaxException {
        log.debug("REST request to update ConfigurationAsso : {}", configurationAssoDTO);
        ConfigurationAssoDTO result = configurationAssoService.save(configurationAssoDTO, signature);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationAssoDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/configuration-assos")
    public ResponseEntity<ConfigurationAssoDTO> getConfigurationAsso() {
        log.debug("REST request to get ConfigurationAsso");
        ConfigurationAssoDTO configurationAssoDTO = configurationAssoService.getConfigurationAsso();
        return ResponseEntity.ok(configurationAssoDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/configuration-assos/signature")
    public void downloadFile(HttpServletResponse response) {
        this.configurationAssoService.getSignature(response);
    }
}
