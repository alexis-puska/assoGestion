package fr.iocean.asso.web.rest.errors;

public class AbsenceUserNotMatchException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public AbsenceUserNotMatchException() {
        super(
            ErrorConstants.ABSENCE_USER_NOT_MATCH,
            "L'utilisateur n'est pas celui de l'absence",
            "configurationAsso",
            "absence-user-not-match"
        );
    }
}
