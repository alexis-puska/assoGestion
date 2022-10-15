package fr.iocean.asso.service.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import fr.iocean.asso.service.pdf.annotation.PdfField;
import fr.iocean.asso.service.pdf.processor.TableProcessor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class PdfService {

    private final Logger log = LoggerFactory.getLogger(PdfService.class);

    private final SpringTemplateEngine templateEngine;

    public PdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @SuppressWarnings("unchecked")
    public <T> void fillPDF(
        String template,
        OutputStream output,
        Object obj,
        Class<T> class1,
        InputStream signature,
        int page,
        int posX,
        int posY
    ) throws DocumentException, IOException {
        // load pdf
        PdfReader reader = new PdfReader(template);
        PdfStamper stamper = new PdfStamper(reader, output);
        // formulaire non éditable
        stamper.setFormFlattening(true);
        BaseFont font = BaseFont.createFont();
        AcroFields fields = stamper.getAcroFields();
        fields.addSubstitutionFont(font);

        // impression champs disponible dans le pdf
        // Set<String> f = fields.getFields().keySet();
        // f.stream().forEach(s -> {
        // // impression des valeurs posibles pour un champ specifique
        // log.info("CHAMPS PDF : {}, valeur possible : {}", s,
        // Arrays.stream(fields.getAppearanceStates(s)).collect(Collectors.joining(" ,
        // ")));
        // });

        Map<String, Field> fieldsOfDocument = getDocumentField(class1);
        for (Map.Entry<String, Field> entry : fieldsOfDocument.entrySet()) {
            try {
                Field field = entry.getValue();
                if (field.get((T) obj) != null) {
                    fields.setField(entry.getKey(), field.get((T) obj).toString());
                    fields.setGenerateAppearances(true);
                }
            } catch (IllegalArgumentException e) {
                log.error("FillPDF IllegalArgumentException : {}", e.getMessage());
            } catch (IllegalAccessException e) {
                log.error("FillPDF IllegalAccessException : {}", e.getMessage());
            }
        }

        if (signature != null) {
            addSignature(stamper, signature, page, posX, posY);
        }
        stamper.close();
    }

    public <T> void fillPDF(String template, OutputStream output, Object obj, Class<T> class1) throws DocumentException, IOException {
        this.fillPDF(template, output, obj, class1, null, 0, 0, 0);
    }

    private void addSignature(PdfStamper stamper, InputStream signature, int page, int posX, int posY)
        throws MalformedURLException, IOException, DocumentException {
        if (signature != null) {
            PdfContentByte over = stamper.getOverContent(page);
            Image image = Image.getInstance(IOUtils.toByteArray(signature));
            image.setAbsolutePosition(posX, posY);
            over.addImage(image);
        }
    }

    private <T> Map<String, Field> getDocumentField(Class<T> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            PdfField excelField = field.getAnnotation(PdfField.class);
            if (excelField != null) {
                field.setAccessible(true);
                fieldMap.put(excelField.name(), field);
            }
        }
        return fieldMap;
    }

    public void printPDF(
        OutputStream outputStream,
        Rectangle format,
        Context context,
        String template,
        CssFile css,
        PdfFooter footer,
        PdfHeader header,
        boolean dontCutTableAsPossible
    ) throws DocumentException, IOException {
        // creation du contenu
        String content = templateEngine.process(template, context);
        log.trace("result template pdf : \n{}", content);

        InputStream is = new ByteArrayInputStream(content.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // step 1 : creation document
        Document document = new Document(format);

        // step 2 : creation imprimante virtuel
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setInitialLeading(12.5f);
        writer.setPageEvent(footer);
        if (header != null) {
            writer.setPageEvent(header);
        }

        // step 3 : ouverture document
        document.open();
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
        if (dontCutTableAsPossible) {
            // custom processor pour les tables, permet de garder une ligne sur une page
            // sans la couper.
            tagProcessorFactory.addProcessor(new TableProcessor(), HTML.Tag.TABLE);
        }
        htmlContext.setTagFactory(tagProcessorFactory);
        htmlContext.setImageProvider(new Base64ImageProvider());

        // step 4 : recherche fichier CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        cssResolver.addCss(css);

        // step 5 : creation flux traitement
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer)));

        // step 6 : transformation html en pdf
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(worker);

        // step 7 : parsing, ecriture document, fermeture flux
        p.parse(is);
        document.close();
        baos.writeTo(outputStream);
        baos.close();
        is.close();
    }

    public CssFile getCssFile(String css) {
        InputStream csspathtest = Thread.currentThread().getContextClassLoader().getResourceAsStream(css);
        return XMLWorkerHelper.getCSS(csspathtest);
    }
}
