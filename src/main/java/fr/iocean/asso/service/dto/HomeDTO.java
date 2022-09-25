package fr.iocean.asso.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HomeDTO implements Serializable {

    private static final long serialVersionUID = 375894372136218784L;

    private long nbCliniqueVeterinaire;
    private long nbChat;
    private long nbPointNourrissage;
    private long nbPointCapture;
    private long nbFamilleAccueil;
    private long nbDonateur;
}
