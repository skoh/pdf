package org.oh.pdf;

import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterTagHelper;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * AES-128 알고리즘 적용
 * 
 * @author skoh
 */
public class JRPdfExporterTagHelper2 extends JRPdfExporterTagHelper {
	protected JRPdfExporterTagHelper2(JRPdfExporter exporter) {
		super(exporter);
	}

	protected void setPdfWriter(PdfWriter pdfWriter) {
		super.setPdfWriter(pdfWriter);
	}

	protected void init(PdfContentByte pdfContentByte) {
		super.init(pdfContentByte);
	}
}
