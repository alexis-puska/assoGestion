package fr.iocean.asso.service;

import fr.iocean.asso.config.ApplicationProperties;
import fr.iocean.asso.domain.enumeration.FileEnum;
import fr.iocean.asso.service.exception.FileAccessException;
import fr.iocean.asso.service.exception.FileAlreadyExistException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final ApplicationProperties applicationProperties;
    private String rootPath;

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private static final String[] IMAGE_EXTENSIONS = { "png", "jpg", "jpeg" };

    public FileService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    //     lor
    //     http://localhost:8081/api/account?Authorization=Bearer%20eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTUwNzg4NDc5OH0.hYdmlEf_J7XBjYhGAiON9kJXzk7eVGrysI7-5auPtdyP9iRkhLoxuiey8ybC_C7MhAc4sS23gB7ketnQWNYjYQ
    //

    @PostConstruct
    private void showInfosFS() throws IOException {
        rootPath = applicationProperties.getFilepath();
        log.info(rootPath);
        Files.createDirectories(Paths.get(rootPath));
    }

    public void saveFiles(FileEnum type, Long id, boolean allowUpdateFile, MultipartFile... multipartFiles) {
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile == null) {
                    continue;
                }
                Path path = getPath(type, id);
                log.debug("path : {}", path);
                createFile(path, multipartFile, false, allowUpdateFile);
            }
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    public void saveImage(FileEnum type, Long id, boolean resize, int width, MultipartFile multipartFile) {
        try {
            if (multipartFile != null) {
                if (!isImage(multipartFile)) {
                    throw new IllegalArgumentException(
                        "The uploaded file is not an image, its extension is not in : " + Arrays.asList(IMAGE_EXTENSIONS)
                    );
                }

                Path path = getPath(type, id);
                Path targetFile = createFile(path, multipartFile, false, true);
                if (resize) {
                    resizeImage(targetFile, width);
                }
            }
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    public File getFile(FileEnum type, Long id, String name) {
        Path path = generatePath(type, id, name);
        if (path.toFile().exists()) {
            return path.toFile();
        } else {
            return null;
        }
    }

    public File createFile(FileEnum type, Long id, String name) throws IOException {
        Path path = generateFolderPath(type, id);
        // verification de l'existance du dossier, si inéxistant on le créer
        if (!path.toFile().exists()) {
            Files.createDirectories(path);
        }
        path = generatePath(type, id, name);
        return new File(path.toString());
    }

    public String getClasspathFileBase64(String resource) {
        try {
            return encodeFileToBase64Binary(ResourceUtils.getFile(resource));
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException : {}", e);
        }
        return "";
    }

    public String getFileBase64(FileEnum type, Long id, String name) {
        Path path = generatePath(type, id, name);
        if (path.toFile().exists()) {
            return encodeFileToBase64Binary(path.toFile());
        } else {
            return null;
        }
    }

    private String encodeFileToBase64Binary(File file) {
        String encoded = null;
        try (InputStream finput = new FileInputStream(file)) {
            byte[] imageBytes = new byte[(int) file.length()];
            finput.read(imageBytes, 0, imageBytes.length);
            encoded = Base64Utils.encodeToString(imageBytes);
        } catch (FileNotFoundException e) {
            log.info("file not found : {}", e);
        } catch (IOException e) {
            log.info("IOException : {}", e);
        }
        return encoded;
    }

    public void deleteFile(FileEnum type, Long id, String name) {
        Path path = generatePath(type, id, name);
        if (path.toFile().exists()) {
            try {
                Files.delete(path);
                log.info("fichier effacer : {}", path);
            } catch (IOException e) {
                log.info("fichier non effacer : {}", path);
            }
            deleteFolder(type, id, false);
        }
    }

    public void deleteFolder(FileEnum type, Long id, boolean force) {
        Path path = generateFolderPath(type, id);
        if ((path.toFile().isDirectory() && path.toFile().listFiles().length == 0) || force) {
            try {
                FileUtils.deleteDirectory(path.toFile());
                log.info("dossier effacer : {}", path);
            } catch (IOException e) {
                log.info("Erreur lors de la suppression du répertoire  : {} {}", path, e.getMessage());
            }
        }
    }

    /**
     * Checks if a multipart file is an image based on its extension, which means
     * it's .png
     *
     * @param multipartFile the multipart file to check the type on
     * @return true if the multipart file is an image, false otherwise
     */
    private static boolean isImage(final MultipartFile multipartFile) {
        boolean originalfileNameExtensionIsAllowed = FilenameUtils.isExtension(
            multipartFile.getOriginalFilename().toLowerCase(),
            IMAGE_EXTENSIONS
        );
        boolean contentTypeIsImage = multipartFile.getContentType().startsWith("image");

        return originalfileNameExtensionIsAllowed && contentTypeIsImage;
    }

    /**
     * Retourne la liste des noms de fichiers pour un type donnée
     *
     * @param id       Id de l'entité
     * @param type     type de l'entité
     * @param fileType type de fichier
     * @return liste de nom
     */
    public List<String> getFilename(Long id, FileEnum type) {
        Path path = getPath(type, id);
        List<String> filenames = new ArrayList<>();
        for (Path p : getFilesPath(path)) {
            if (!p.getFileName().toString().startsWith(".nfs")) {
                filenames.add(p.getFileName().toString());
            }
        }
        return filenames;
    }

    /**
     * Retourne la liste des noms de fichiers pour un type donnée
     *
     * @param id       Id de l'entité
     * @param type     type de l'entité
     * @param fileType type de fichier
     * @return liste de nom
     */
    public List<String> getFilenameWithoutFolder(Long id, FileEnum type) {
        Path path = getPath(type, id);
        List<String> filenames = new ArrayList<>();
        for (Path p : getFilesPath(path)) {
            if (!p.toFile().isDirectory() && !p.getFileName().toString().startsWith(".nfs")) {
                filenames.add(p.getFileName().toString());
            }
        }
        return filenames;
    }

    public Path getPath(FileEnum type, Long id) {
        List<String> argList = new ArrayList<>();
        argList.add(type.getDirectory());
        if (id != null) {
            argList.add(id.toString());
        }
        String[] args = new String[argList.size()];
        args = argList.toArray(args);
        return Paths.get(rootPath, args);
    }

    private Path generatePath(FileEnum type, Long id, String fileName) {
        List<String> argList = new ArrayList<>();
        argList.add(type.getDirectory());
        if (id != null) {
            argList.add(id.toString());
        }
        argList.add(fileName);
        String[] args = new String[argList.size()];
        args = argList.toArray(args);
        return Paths.get(rootPath, args);
    }

    private Path generateFolderPath(FileEnum type, Long id) {
        List<String> argList = new ArrayList<>();
        argList.add(type.getDirectory());
        if (id != null) {
            argList.add(id.toString());
        }
        String[] args = new String[argList.size()];
        args = argList.toArray(args);
        return Paths.get(rootPath, args);
    }

    private List<Path> getFilesPath(Path path) {
        try (Stream<Path> list = Files.list(path)) {
            return list.collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void deleteAllIn(Path path) {
        try {
            FileUtils.deleteDirectory(path.toFile());
        } catch (IOException e) {
            log.error("Can not delete files : {}", e.getMessage());
        }
    }

    private Path createFile(Path path, MultipartFile multipartFile, boolean deleteDirectories, boolean allowUpdateFile) throws IOException {
        log.debug("in create file : File Service : {}", multipartFile.getOriginalFilename());
        if (deleteDirectories) {
            deleteAllIn(path);
            Files.createDirectories(path);
        }

        // verification de l'existance du dossier, si inéxistant on le créer
        if (!path.toFile().exists()) {
            Files.createDirectories(path);
        }

        String fileName = multipartFile.getOriginalFilename();

        // Contournement IE
        if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
        }

        Path targetFile = path.resolve(fileName);

        // fichiers deja existant : mise a jour
        if (targetFile.toFile().exists()) {
            if (allowUpdateFile) {
                Files.delete(targetFile);
            } else {
                throw new FileAlreadyExistException();
            }
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, targetFile);
            log.debug("Saving file on disk with path : {}", targetFile);
        }
        return targetFile;
    }

    private void resizeImage(Path path, int width) {
        try {
            BufferedImage originalImage = ImageIO.read(path.toFile());
            int newHeight = (int) (((double) originalImage.getHeight()) * (((double) width) / ((double) originalImage.getWidth())));

            BufferedImage resizedImage = Scalr.resize(
                originalImage,
                Scalr.Method.QUALITY,
                Scalr.Mode.AUTOMATIC,
                width,
                newHeight,
                Scalr.OP_ANTIALIAS
            );

            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, width, newHeight, null);

            ImageIO.write(resizedImage, FilenameUtils.getExtension(path.getFileName().toString()), new File(path.toString()));
        } catch (IOException e) {
            log.error("Error while resizing image : {}", e);
        }
    }

    public static class FilenameAndInputStream {

        private String filename;
        private InputStream inputStream;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public FilenameAndInputStream(String filename, InputStream inputStream) {
            super();
            this.filename = filename;
            this.inputStream = inputStream;
        }
    }
}
