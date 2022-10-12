package fr.iocean.asso.service.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author apuskarczyk Impression du footer dans un fichier pdf Chaine de
 *         charactère paramétrable --- numerotation page en bas à droite
 */
public class PdfHeader extends PdfPageEventHelper {

    private boolean printOnFirstPage;
    private Image logo;

    public PdfHeader(boolean printOnFirstPage, Image logo) {
        this.printOnFirstPage = printOnFirstPage;
        this.logo = logo;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (printOnFirstPage == true || (printOnFirstPage == false && writer.getPageNumber() != 1)) {
            PdfContentByte cb = writer.getDirectContent();
            try {
                logo.scaleToFit(40, 40);
                logo.setAbsolutePosition(510, 790);
                cb.addImage(logo);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
}
