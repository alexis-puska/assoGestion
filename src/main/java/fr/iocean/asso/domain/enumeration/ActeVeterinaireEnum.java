package fr.iocean.asso.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActeVeterinaireEnum {
    STERILISATION("Stérilisation"),

    IDENTIFICATION("Identification"),

    VACCINATION_TL("Vaccination Typhus / Leucose"),
    VACCINATION_TLC("Vaccination Typhus / Leucose / Choryza"),
    VACCINATION_TL_PRIMO("Primo vaccination Typhus / Leucose"),
    VACCINATION_TLC_PRIMO("Primo vaccination Typhus / Leucose/ Choryza"),

    DEPARASITAGE("Déparasitage"),
    ANTIPUCE_ANTI_TIQUE("Anti-puce / Anti-tique"),

    VERMIFUGATION("Vermifuge"),

    OPERATION("Opération"),
    AUTRES("Identification");

    private String nomActe;
}
