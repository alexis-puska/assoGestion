package fr.iocean.asso.web.rest.errors;

public class SignatureSizeException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public SignatureSizeException() {
        super(ErrorConstants.SIGNATURE_SIZE, "La signature ne doit pas dépasser 150 x 70!", "configurationDon", "signatureSize");
    }
}
