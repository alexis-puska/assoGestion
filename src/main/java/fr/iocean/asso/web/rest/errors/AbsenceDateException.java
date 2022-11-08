package fr.iocean.asso.web.rest.errors;

public class AbsenceDateException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public AbsenceDateException() {
        super(
            ErrorConstants.ABSENCE_DATE_EXCEPTION,
            "L'utilisateur n'est pas celui de l'absence",
            "configurationAsso",
            "absence-date-exception"
        );
    }
}
