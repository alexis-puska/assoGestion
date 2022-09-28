package fr.iocean.asso.service.exception;

import java.io.IOException;

public class FileAccessException extends RuntimeException {

    private static final long serialVersionUID = -1948264222794982541L;

    public FileAccessException(IOException e) {
        super("File access exception!", e);
    }
}
