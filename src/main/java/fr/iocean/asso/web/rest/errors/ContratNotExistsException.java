package fr.iocean.asso.web.rest.errors;

public class ContratNotExistsException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public ContratNotExistsException() {
        super(ErrorConstants.CONTRAT_NOT_EXISTS, "Le chat n'est pas trouvé", "configurationAsso", "contratNotExists");
    }
}
