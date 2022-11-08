package fr.iocean.asso.web.rest.errors;

public class AbsenceNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public AbsenceNotFoundException() {
        super(ErrorConstants.ABSENCE_NOT_FOUND, "Absence non trouvée", "configurationAsso", "absence-not-found");
    }
}
