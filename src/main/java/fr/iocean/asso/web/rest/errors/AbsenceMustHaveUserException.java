package fr.iocean.asso.web.rest.errors;

public class AbsenceMustHaveUserException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public AbsenceMustHaveUserException() {
        super(ErrorConstants.ABSENCE_MUST_HAVE_USER, "Absence doit avoir un utilisateur", "configurationAsso", "absence-must-have-user");
    }
}
