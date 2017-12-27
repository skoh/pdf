package org.oh.pdf.web;

import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import org.oh.pdf.JRPdfExporter2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.itextpdf.text.pdf.PdfWriter;

/**
 * Spring Framework PDF View(4단계)
 * 
 * @author skoh
 */
@Component
public class PdfView4 extends JasperReportsPdfView {
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	protected net.sf.jasperreports.engine.JRExporter createExporter() {
		net.sf.jasperreports.engine.JRExporter exporter = new JRPdfExporter2();

		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		configuration.isCreatingBatchModeBookmarks();
		configuration.setEncrypted(true);
		configuration.set128BitKey(true);
		configuration.setUserPassword("1");
		configuration.setOwnerPassword("1");
		configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
		exporter.setConfiguration(configuration);

		return exporter;
	}
}
