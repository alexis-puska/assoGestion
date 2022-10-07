package fr.iocean.asso.service.pdf.processor;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.html.table.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TableProcessor extends Table {

    private boolean keepTogether;
    private boolean splitRows;
    private boolean splitLate;

    public TableProcessor() {
        this.keepTogether = true;
        this.splitRows = true;
        this.splitLate = true;
    }

    @Override
    public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
        List<Element> result = super.end(ctx, tag, currentContent);
        PdfPTable table = (PdfPTable) result.get(0);
        // if not set, table **may** be forwarded to next page
        table.setKeepTogether(keepTogether);
        // next two properties keep <tr> together if possible
        table.setSplitRows(splitRows);
        table.setSplitLate(splitLate);
        List<Element> r = new ArrayList<>();
        r.add(table);
        return r;
    }
}
