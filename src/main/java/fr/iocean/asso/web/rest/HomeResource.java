package fr.iocean.asso.web.rest;

import fr.iocean.asso.service.HomeService;
import fr.iocean.asso.service.dto.HomeDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link fr.iocean.asso.domain.RaceChat}.
 */
@RestController
@RequestMapping("/api")
public class HomeResource {

    private final HomeService homeService;

    public HomeResource(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home/count")
    public HomeDTO getCount() {
        return homeService.getCount();
    }
}
