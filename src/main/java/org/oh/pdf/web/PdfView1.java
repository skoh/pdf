package org.oh.pdf.web;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oh.pdf.Pdf;
import org.oh.pdf.PdfUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Spring Framework PDF View(1단계)
 * 
 * @author skoh
 */
@Component
public class PdfView1 extends PdfView2 {
	/**
	 * PDF 파일 초기화
	 */
	@Override
	protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Pdf pdf = (Pdf) model.get("pdf");

		// IE workaround: write into byte array first.
		ByteArrayOutputStream baos = createTemporaryOutputStream();

		// Apply preferences and build metadata.
		PdfReader reader = new PdfReader(pdf.getPdfFilePath());
		PdfStamper stamper = new PdfStamper(reader, baos);
		PdfWriter writer = stamper.getWriter();
		prepareWriter(model, writer, request);

		// Build PDF document.
		buildPdfStamper(model, stamper, request, response);
		stamper.close();
		reader.close();

		// Flush to HTTP response.
		writeToResponse(response, baos);
	}

	/**
	 * PDF 파일 제작
	 * 
	 * @param model the model Map
	 * @param stamper the iText PdfStamper to add elements to
	 * @param request in case we need locale etc. Shouldn't look at attributes.
	 * @param response in case we need to set cookies. Shouldn't write to it.
	 * 
	 * @throws Exception any exception that occurred during document building
	 */
	protected void buildPdfStamper(Map<String, Object> model, PdfStamper stamper, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Pdf pdf = (Pdf) model.get("pdf");

		if ("true".equals(request.getParameter("down")))
			response.setHeader("Content-Disposition", "attachment; filename=\"" + pdf.getFilePath() + "\"");

		PdfUtils.buildPdfStamper(pdf, stamper);
	}

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	}
}
