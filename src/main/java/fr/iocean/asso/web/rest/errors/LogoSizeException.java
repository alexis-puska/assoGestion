package fr.iocean.asso.web.rest.errors;

public class LogoSizeException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LogoSizeException() {
        super(ErrorConstants.SIGNATURE_SIZE, "Le logo ne doit pas dépasser 400 x 400!", "configurationAsso", "logoSize");
    }
}
