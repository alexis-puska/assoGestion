package fr.iocean.asso.web.rest;

import fr.iocean.asso.service.ConfigurationDonService;
import fr.iocean.asso.service.dto.ConfigurationDonDTO;
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
 * REST controller for managing {@link fr.iocean.asso.domain.ConfigurationDon}.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationDonResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationDonResource.class);

    private static final String ENTITY_NAME = "configurationDon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationDonService configurationDonService;

    public ConfigurationDonResource(ConfigurationDonService configurationDonService) {
        this.configurationDonService = configurationDonService;
    }

    @PutMapping("/configuration-dons")
    public ResponseEntity<ConfigurationDonDTO> updateConfigurationDon(
        @RequestPart(name = "configurationDon") ConfigurationDonDTO configurationDonDTO,
        @RequestPart(name = "signature", required = false) MultipartFile signature
    ) throws URISyntaxException {
        log.debug("REST request to update ConfigurationDon : {}", configurationDonDTO);
        ConfigurationDonDTO result = configurationDonService.save(configurationDonDTO, signature);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationDonDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/configuration-dons")
    public ResponseEntity<ConfigurationDonDTO> getConfigurationDon() {
        log.debug("REST request to get ConfigurationDon");
        ConfigurationDonDTO configurationDonDTO = configurationDonService.getConfigurationDon();
        return ResponseEntity.ok(configurationDonDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/configuration-dons/signature")
    public void downloadFile(HttpServletResponse response) {
        this.configurationDonService.getSignature(response);
    }
}
