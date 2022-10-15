package fr.iocean.asso.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String NOURRISSEUR = "ROLE_NOURRISSEUR";

    public static final String FAMILLE_ACCUEIL = "ROLE_FAMILLE_ACCUEIL";

    public static final String BENEVOLE = "ROLE_BENEVOLE";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
