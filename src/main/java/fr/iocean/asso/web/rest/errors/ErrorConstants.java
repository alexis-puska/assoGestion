package fr.iocean.asso.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI SIGNATURE_SIZE = URI.create(PROBLEM_BASE_URL + "/signature-size");
    public static final URI LOGO_SIZE = URI.create(PROBLEM_BASE_URL + "/signature-size");
    public static final URI CHAT_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/chat-not-found");
    public static final URI CONTRAT_NOT_EXISTS = URI.create(PROBLEM_BASE_URL + "/contrat-not-exists");
    public static final URI ABSENCE_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/absence-not-found");
    public static final URI ABSENCE_MUST_HAVE_USER = URI.create(PROBLEM_BASE_URL + "/absence-must-have-user");
    public static final URI ABSENCE_USER_NOT_MATCH = URI.create(PROBLEM_BASE_URL + "/absence-user-not-match");
    public static final URI ABSENCE_DATE_EXCEPTION = URI.create(PROBLEM_BASE_URL + "/absence-date-exception");

    private ErrorConstants() {}
}
