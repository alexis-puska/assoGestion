package fr.iocean.asso.service.dto;

import fr.iocean.asso.domain.enumeration.PaiementEnum;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContratPdfDTO implements Serializable {

    private static final long serialVersionUID = -3622673342739155453L;

    // Asso
    private String denominationAsso;
    private String adresseAsso;
    private String telephoneAsso;
    private String emailAsso;

    // Adoptant
    private String nomAdoptant;
    private String adresseAdoptant;
    private String telephoneAdoptant;
    private String emailAdoptant;

    // Chat
    private String nomChat;
    private String raceChat;
    private LocalDate dateNaissanceChat;
    private String sexeChat;
    private String couleurChat;

    // Identification
    private LocalDate dateIdentification;
    private boolean tatouage;
    private boolean puce;
    private String identification;
    private String veterinaire;

    // Sterilisation
    private boolean sterilise;
    private LocalDate dateSterilisation;
    private LocalDate dateSterilisationSouhaitee;

    // primo vaccination
    private boolean primo;
    private LocalDate dateVaccination;
    private LocalDate dateRapelleVaccination;
    private String typeVaccin;

    private List<String> divers;

    private Double montant;
    private PaiementEnum paiement;

    private LocalDate dateContrat;
}
