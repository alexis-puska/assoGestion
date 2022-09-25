package fr.iocean.asso.service;

import fr.iocean.asso.domain.RaceChat;
import fr.iocean.asso.repository.ChatRepository;
import fr.iocean.asso.repository.CliniqueVeterinaireRepository;
import fr.iocean.asso.repository.DonateurRepository;
import fr.iocean.asso.repository.FamilleAccueilRepository;
import fr.iocean.asso.repository.PointCaptureRepository;
import fr.iocean.asso.repository.PointNourrissageRepository;
import fr.iocean.asso.service.dto.HomeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RaceChat}.
 */
@Service
@Transactional
public class HomeService {

    private final CliniqueVeterinaireRepository cliniqueVeterinaireRepository;
    private final ChatRepository chatRepository;
    private final DonateurRepository donateurRepository;
    private final FamilleAccueilRepository familleAccueilRepository;
    private final PointCaptureRepository pointCaptureRepository;
    private final PointNourrissageRepository pointNourrissageRepository;

    public HomeService(
        CliniqueVeterinaireRepository cliniqueVeterinaireRepository,
        ChatRepository chatRepository,
        DonateurRepository donateurRepository,
        FamilleAccueilRepository familleAccueilRepository,
        PointCaptureRepository pointCaptureRepository,
        PointNourrissageRepository pointNourrissageRepository
    ) {
        this.cliniqueVeterinaireRepository = cliniqueVeterinaireRepository;
        this.chatRepository = chatRepository;
        this.donateurRepository = donateurRepository;
        this.familleAccueilRepository = familleAccueilRepository;
        this.pointCaptureRepository = pointCaptureRepository;
        this.pointNourrissageRepository = pointNourrissageRepository;
    }

    public HomeDTO getCount() {
        HomeDTO dto = new HomeDTO();
        dto.setNbCliniqueVeterinaire(this.cliniqueVeterinaireRepository.count());
        dto.setNbChat(this.chatRepository.count());
        dto.setNbDonateur(this.donateurRepository.count());
        dto.setNbFamilleAccueil(this.familleAccueilRepository.count());
        dto.setNbPointCapture(this.pointCaptureRepository.count());
        dto.setNbPointNourrissage(this.pointNourrissageRepository.count());
        return dto;
    }
}
