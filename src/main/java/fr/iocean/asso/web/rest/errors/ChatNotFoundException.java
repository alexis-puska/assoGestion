package fr.iocean.asso.web.rest.errors;

public class ChatNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public ChatNotFoundException() {
        super(ErrorConstants.CHAT_NOT_FOUND, "Le chat n'est pas trouvé", "configurationAsso", "chatNotFound");
    }
}
