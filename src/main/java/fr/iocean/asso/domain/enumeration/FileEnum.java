package fr.iocean.asso.domain.enumeration;

public enum FileEnum {
    LOGO_ASSO("logoAsso"),
    SIGNATURE_ASSO("signatureAsso"),
    PHOTO_CHAT("photoChat"),
    ALBUM_CHAR("albumChat");

    private String directory;

    private FileEnum(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }
}
