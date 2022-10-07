package fr.iocean.asso.service.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Base64ImageProvider extends AbstractImageProvider {

    private final Logger log = LoggerFactory.getLogger(Base64ImageProvider.class);

    @Override
    public Image retrieve(String src) {
        int pos = src.indexOf("base64,");
        try {
            if (src.startsWith("data") && pos > 0) {
                byte[] img = Base64.decode(src.substring(pos + 7));
                return Image.getInstance(img);
            } else {
                return Image.getInstance(src);
            }
        } catch (BadElementException ex) {
            log.error("BadElementException : {}", ex);
        } catch (IOException ex) {
            log.error("error : {}", ex);
        }
        return null;
    }

    @Override
    public String getImageRootPath() {
        return null;
    }
}
