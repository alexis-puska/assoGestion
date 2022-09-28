package fr.iocean.asso.service.exception;

public class FileAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileAlreadyExistException() {
        super("File already exist!");
    }
}
