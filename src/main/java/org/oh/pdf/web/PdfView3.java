package org.oh.pdf.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oh.pdf.Pdf;
import org.oh.pdf.PdfUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Spring Framework PDF View(3단계)
 * 
 * @author skoh
 */
@Component
public class PdfView3 extends PdfView2 {
	/**
	 * PDF 파일 제작
	 * 
	 * @param model {@inheritDoc}
	 * @param document {@inheritDoc}
	 * @param writer {@inheritDoc}
	 * @param request {@inheritDoc}
	 * @param response {@inheritDoc}
	 * 
	 * @throws Exception {@inheritDoc}
	 */
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Pdf pdf = (Pdf) model.get("pdf");

		if ("true".equals(request.getParameter("down")))
			response.setHeader("Content-Disposition", "attachment; filename=\"" + pdf.getFilePath() + "\"");

		PdfUtils.buildPdfDocument3(pdf, document, writer);
	}
}
