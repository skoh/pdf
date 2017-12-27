package org.oh.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRPdfaIccProfileNotFoundException;
import net.sf.jasperreports.engine.util.NullOutputStream;
import net.sf.jasperreports.export.ExportInterruptedException;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.type.PdfPrintScalingEnum;
import net.sf.jasperreports.export.type.PdfVersionEnum;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;
import net.sf.jasperreports.repo.RepositoryUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfICCBased;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * AES-128 알고리즘 적용
 * 
 * @author skoh
 */
public class JRPdfExporter2 extends JRPdfExporter {
	protected JRPdfExporterTagHelper2 tagHelper = new JRPdfExporterTagHelper2(this);

	@SuppressWarnings({ "deprecation" })
	public void setParameter(net.sf.jasperreports.engine.JRExporterParameter parameter, Object value) {
//		checkApi(true);

		parameters.put(parameter, value);
		exporterInput = null;
		exporterOutput = null;
		exporterConfiguration = null;
	}

	protected void exportReportToStream(OutputStream os) throws JRException {
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfExporterConfiguration configuration = getCurrentConfiguration();

		pageFormat = jasperPrint.getPageFormat(0);

		document = new Document(new Rectangle(pageFormat.getPageWidth(), pageFormat.getPageHeight()));

		// accessibility check: unwanted default document tag
		document.setRole(null);

		imageTesterDocument = new Document(new Rectangle(10, // jasperPrint.getPageWidth(),
				10 // jasperPrint.getPageHeight()
				));

		boolean closeDocuments = true;
		try {
			PdfaConformanceEnum pdfaConformance = configuration.getPdfaConformance();
			boolean gotPdfa = false;
			if (PdfaConformanceEnum.PDFA_1A == pdfaConformance) {
				pdfWriter = PdfAWriter.getInstance(document, os, PdfAConformanceLevel.PDF_A_1A);
				tagHelper.setConformanceLevel(PdfAConformanceLevel.PDF_A_1A);
				gotPdfa = true;
			} else if (PdfaConformanceEnum.PDFA_1B == pdfaConformance) {
				pdfWriter = PdfAWriter.getInstance(document, os, PdfAConformanceLevel.PDF_A_1B);
				tagHelper.setConformanceLevel(PdfAConformanceLevel.PDF_A_1B);
				gotPdfa = true;
			} else {
				pdfWriter = PdfWriter.getInstance(document, os);
			}
			pdfWriter.setCloseStream(false);

			tagHelper.setPdfWriter(pdfWriter);

			PdfVersionEnum pdfVersion = configuration.getPdfVersion();
			if (pdfVersion != null) {
				pdfWriter.setPdfVersion(pdfVersion.getName().charAt(0));
			}
			if (configuration.isCompressed()) {
				pdfWriter.setFullCompression();
			}
			if (configuration.isEncrypted()) {
				int perms = configuration.isOverrideHints() == null || configuration.isOverrideHints() ? (configuration
						.getPermissions() != null ? (Integer) configuration.getPermissions() : permissions)
						: (permissions != 0 ? permissions
								: (configuration.getPermissions() != null ? (Integer) configuration.getPermissions()
										: 0));

				// AES-128 알고리즘 적용 by skoh
//				pdfWriter.setEncryption(configuration.is128BitKey(), configuration.getUserPassword(),
//						configuration.getOwnerPassword(), perms);
				pdfWriter.setEncryption(configuration.getUserPassword().getBytes(), configuration.getUserPassword()
						.getBytes(), perms, PdfWriter.ENCRYPTION_AES_128);
			}

			PdfPrintScalingEnum printScaling = configuration.getPrintScaling();
			if (PdfPrintScalingEnum.DEFAULT == printScaling) {
				pdfWriter.addViewerPreference(PdfName.PRINTSCALING, PdfName.APPDEFAULT);
			} else if (PdfPrintScalingEnum.NONE == printScaling) {
				pdfWriter.addViewerPreference(PdfName.PRINTSCALING, PdfName.NONE);
			}

			// Add meta-data parameters to generated PDF document
			// mtclough@users.sourceforge.net 2005-12-05
			String title = configuration.getMetadataTitle();
			if (title != null) {
				document.addTitle(title);
				if (configuration.isDisplayMetadataTitle()) {
					pdfWriter.addViewerPreference(PdfName.DISPLAYDOCTITLE, new PdfBoolean(true));
				}
			}
			String author = configuration.getMetadataAuthor();
			if (author != null) {
				document.addAuthor(author);
			}
			String subject = configuration.getMetadataSubject();
			if (subject != null) {
				document.addSubject(subject);
			}
			String keywords = configuration.getMetadataKeywords();
			if (keywords != null) {
				document.addKeywords(keywords);
			}
			String creator = configuration.getMetadataCreator();
			if (creator != null) {
				document.addCreator(creator);
			} else {
				document.addCreator("JasperReports Library version "
						+ Package.getPackage("net.sf.jasperreports.engine").getImplementationVersion());
			}

			// accessibility check: tab order follows the structure of the document
			pdfWriter.setTabs(PdfName.S);

			// accessibility check: setting the document primary language
			String language = configuration.getTagLanguage();
			if (language != null) {
				pdfWriter.getExtraCatalog().put(PdfName.LANG, new PdfString(language));
			}
			// BEGIN: PDF/A support
			if (gotPdfa) {
				pdfWriter.createXmpMetadata();
			} else {
				pdfWriter.setRgbTransparencyBlending(true);
			}
			// END: PDF/A support

			document.open();
			// BEGIN: PDF/A support
			if (gotPdfa) {
				String iccProfilePath = configuration.getIccProfilePath();
				if (iccProfilePath != null) {
					PdfDictionary pdfDictionary = new PdfDictionary(PdfName.OUTPUTINTENT);
					pdfDictionary.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("sRGB IEC61966-2.1"));
					pdfDictionary.put(PdfName.INFO, new PdfString("sRGB IEC61966-2.1"));
					pdfDictionary.put(PdfName.S, PdfName.GTS_PDFA1);
					InputStream iccIs = RepositoryUtil.getInstance(jasperReportsContext).getInputStreamFromLocation(
							iccProfilePath);
					PdfICCBased pdfICCBased = new PdfICCBased(ICC_Profile.getInstance(iccIs));
					pdfICCBased.remove(PdfName.ALTERNATE);
					pdfDictionary.put(PdfName.DESTOUTPUTPROFILE, pdfWriter.addToBody(pdfICCBased)
							.getIndirectReference());

					pdfWriter.getExtraCatalog().put(PdfName.OUTPUTINTENTS, new PdfArray(pdfDictionary));
				} else {
					throw new JRPdfaIccProfileNotFoundException();
				}
			}
			// END: PDF/A support

			String pdfJavaScript = configuration.getPdfJavaScript();
			if (pdfJavaScript != null) {
				pdfWriter.addJavaScript(pdfJavaScript);
			}

			pdfContentByte = pdfWriter.getDirectContent();

			tagHelper.init(pdfContentByte);

			PdfWriter imageTesterPdfWriter = PdfWriter.getInstance(imageTesterDocument, new NullOutputStream() // discard the output
					);
			imageTesterDocument.open();
			imageTesterDocument.newPage();
			imageTesterPdfContentByte = imageTesterPdfWriter.getDirectContent();
			imageTesterPdfContentByte.setLiteral("\n");

			List<ExporterInputItem> items = exporterInput.getItems();

			initBookmarks(items);

			boolean isCreatingBatchModeBookmarks = configuration.isCreatingBatchModeBookmarks();

			for (reportIndex = 0; reportIndex < items.size(); reportIndex++) {
				ExporterInputItem item = items.get(reportIndex);

				setCurrentExporterInputItem(item);

				loadedImagesMap = new HashMap<Renderable, Image>();

				pageFormat = jasperPrint.getPageFormat(0);

				setPageSize(null);

				List<JRPrintPage> pages = jasperPrint.getPages();
				if (pages != null && pages.size() > 0) {
					if (items.size() > 1) {
						document.newPage();

						if (isCreatingBatchModeBookmarks) {
							// add a new level to our outline for this report
							addBookmark(0, jasperPrint.getName(), 0, 0);
						}
					}

					PdfReportConfiguration lcItemConfiguration = getCurrentItemConfiguration();

					boolean sizePageToContent = lcItemConfiguration.isSizePageToContent();

					PrintPageFormat oldPageFormat = null;

					PageRange pageRange = getPageRange();
					int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange
							.getStartPageIndex();
					int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1)
							: pageRange.getEndPageIndex();

					for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
						if (Thread.interrupted()) {
							throw new ExportInterruptedException();
						}

						JRPrintPage page = pages.get(pageIndex);

						pageFormat = jasperPrint.getPageFormat(pageIndex);

						if (sizePageToContent || oldPageFormat != pageFormat) {
							setPageSize(sizePageToContent ? page : null);
						}

						document.newPage();

						pdfContentByte = pdfWriter.getDirectContent();

						pdfContentByte.setLineCap(2);// PdfContentByte.LINE_CAP_PROJECTING_SQUARE since iText 1.02b

						writePageAnchor(pageIndex);

						/*   */
						exportPage(page);

						oldPageFormat = pageFormat;
					}
				} else {
					document.newPage();
					pdfContentByte = pdfWriter.getDirectContent();
					pdfContentByte.setLiteral("\n");
				}
			}

			closeDocuments = false;
			document.close();
			imageTesterDocument.close();
		} catch (DocumentException e) {
			throw new JRException("PDF Document error : " + jasperPrint.getName(), e);
		} catch (IOException e) {
			throw new JRException("Error generating PDF report : " + jasperPrint.getName(), e);
		} finally {
			if (closeDocuments) // only on exception
			{
				try {
					document.close();
				} catch (Exception e) {
					// ignore, let the original exception propagate
				}

				try {
					imageTesterDocument.close();
				} catch (Exception e) {
					// ignore, let the original exception propagate
				}
			}
		}

		// return os.toByteArray();
	}
}
