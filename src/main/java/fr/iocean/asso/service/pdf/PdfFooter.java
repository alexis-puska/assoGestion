package fr.iocean.asso.service.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author apuskarczyk Impression du footer dans un fichier pdf Chaine de
 *         charactère paramétrable --- numerotation page en bas à droite
 */
public class PdfFooter extends PdfPageEventHelper {

    private String font;
    private PdfTemplate t;
    private Image total;
    private String footer;
    private int fontSize;
    private boolean printOnFirstPage;
    private boolean landscape;

    public PdfFooter(String footer, int fontSize, boolean printOnFirstPage, boolean landscape) {
        this.footer = footer;
        this.fontSize = fontSize;
        this.font = FontFactory.HELVETICA;
        this.printOnFirstPage = printOnFirstPage;
        this.landscape = landscape;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (printOnFirstPage == true || (printOnFirstPage == false && writer.getPageNumber() != 1)) {
            PdfPTable table = new PdfPTable(2);
            try {
                if (landscape) {
                    table.setWidths(new int[] { 58, 2 });
                    table.setTotalWidth(770);
                } else {
                    table.setWidths(new int[] { 58, 2 });
                    table.setTotalWidth(520);
                }
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(new Phrase(footer, FontFactory.getFont(font, fontSize)));
                table.addCell(new Phrase(String.format("%d", writer.getPageNumber()), FontFactory.getFont(font, fontSize)));
                PdfContentByte canvas = writer.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                table.writeSelectedRows(0, -1, 36, 30, canvas);
                canvas.endMarkedContentSequence();
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(
            t,
            Element.ALIGN_LEFT,
            new Phrase(String.valueOf(writer.getPageNumber()), FontFactory.getFont(font, fontSize)),
            2,
            2,
            0
        );
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}
