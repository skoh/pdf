package org.oh.pdf;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.pdfbox.encoding.WinAnsiEncoding;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.junit.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class PDFbox {
	public PDFbox() {
	}

	public void test01() throws Exception {
		// Create a document and add a page to it
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
		document.addPage(page);

		// Create a new font object selecting one of the PDF base fonts
		PDFont font = PDType1Font.HELVETICA_BOLD;

		// Start a new content stream which will "hold" the to be created content
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
		contentStream.beginText();
		contentStream.setFont(font, 12);
		contentStream.moveTextPositionByAmount(100, 100);
		contentStream.drawString("Hello World");
		contentStream.endText();

		// Make sure that the content stream is closed:
		contentStream.close();

		// Save the results and ensure that the document is properly closed:
		document.save("results/pdfbox/Hello World.pdf");
		document.close();
	}

	@Test
	public void test02() throws Exception {
		run(1, 1);
	}

	@SuppressWarnings("unchecked")
	@Async
	public Future<Double> run(int threadNo, int count) throws Exception {
		StopWatch sw = new StopWatch();
		sw.start();

//		String fileName = "results/pdfbox/Hello World";
		String fileName = "results/pdfbox/홈스쿨 위탁계약서";

		// PDF 갯수
		for (int i = 1; i <= count; i++) {
			// Create a document and add a page to it
			PDDocument document = PDDocument.load(fileName + ".pdf");
			List<PDPage> pageList = document.getDocumentCatalog().getAllPages();

			// Create a new font object selecting one of the PDF base fonts
			PDFont font = PDType1Font.HELVETICA_BOLD;
//			PDSimpleFont font = PDType1Font.TIMES_ROMAN;
			// Create a new font object by loading a TrueType font into the document
//			PDFont font = PDTrueTypeFont.loadTTF(document, "resources/pdfbox/ARIAL.TTF");

//			font.setFontEncoding(new WinAnsiEncoding());
//			font.setFontEncoding(new PdfDocEncoding());

			// Start a new content stream which will "hold" the to be created content
			PDPageContentStream contentStream = new PDPageContentStream(document, pageList.get(0), true, false);

			// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
			contentStream.beginText();
			contentStream.setFont(font, 12);
			contentStream.setNonStrokingColor(Color.RED);
			contentStream.moveTextPositionByAmount(200, 200);
			contentStream.drawString(toUTF_8("Hello World! 안녕하세요!"));
			contentStream.endText();

			InputStream in = new FileInputStream(new File("resources/pdfbox/header_logo.jpg"));
			PDJpeg img = new PDJpeg(document, in);
			contentStream.drawImage(img, 300, 300);

//			BufferedImage awtImage = ImageIO.read(new File("results/pdfbox/header_logo.jpg"));
//			PDXObjectImage ximage = new PDPixelMap(document, awtImage);
//			float scale = 2f; // alter this value to set the image size
//			contentStream.drawXObject(ximage, 300, 300, ximage.getWidth() * scale, ximage.getHeight() * scale);

			contentStream.setLineWidth(2);
			contentStream.setStrokingColor(Color.BLUE);
			contentStream.drawLine(400, 400, 500, 500);

//			contentStream.setNonStrokingColor(Color.BLACK);
//			contentStream.fillRect(500, 500, 100, 100);

			// Make sure that the content stream is closed:
			contentStream.close();

			// Save the results and ensure that the document is properly closed:
			document.save(fileName + (threadNo) + "-" + i + ".pdf");
			document.close();
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
	}

	public static String toUTF_8(String str) {
//		if (isValidate(str)) {
			try {
				return new String(str.getBytes("iso-8859-1"), "ksc5601");
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
//		}

		return str;
	}

//	public void test03() throws Exception {
//		PDDocument doc = PDDocument.load("results/pdfbox/Hello World.pdf");
//
//		int keyLength = 128; // 40 or 128; 256 will be available in version 2.0
//		AccessPermission ap = new AccessPermission();
//
//		// disable printing, everything else is allowed
//		ap.setCanPrint(false);
//
//		// owner password (to open the file with all permissions) is "12345"
//		// user password (to open the file but with restricted permissions, is empty here)
//		StandardProtectionPolicy spp = new StandardProtectionPolicy("12345", "", ap);
//		spp.setEncryptionKeyLength(keyLength);
//		spp.setPermissions(ap);
//		doc.protect(spp);
//
//		doc.save("results/pdfbox/Hello World-encrypted.pdf");
//		doc.close();
//	}

	public void test04() throws Exception {
		PDDocument doc = PDDocument.load("results/pdfbox/Hello World.pdf");

		PDEmbeddedFilesNameTreeNode efTree = new PDEmbeddedFilesNameTreeNode();

		// first create the file specification, which holds the embedded file
		PDComplexFileSpecification fs = new PDComplexFileSpecification();
		fs.setFile("Test.txt");

		// create a dummy file stream, this would probably normally be a FileInputStream
		byte[] data = "This is the contents of the embedded file".getBytes("ISO-8859-1");
		InputStream is = new ByteArrayInputStream(data);

		PDEmbeddedFile ef = new PDEmbeddedFile(doc, is);
		// set some of the attributes of the embedded file
		ef.setSubtype("text/plain");
		ef.setSize(data.length);
		ef.setCreationDate(new GregorianCalendar());
		fs.setEmbeddedFile(ef);

		// now add the entry to the embedded file tree and set in the document.
		Map<String, COSObjectable> efMap = new HashMap<String, COSObjectable>();
		efMap.put("My first attachment", fs);
		efTree.setNames(efMap);
		// attachments are stored as part of the "names" dictionary in the document catalog
		PDDocumentNameDictionary names = new PDDocumentNameDictionary(doc.getDocumentCatalog());
		names.setEmbeddedFiles(efTree);
		doc.getDocumentCatalog().setNames(names);

		// Save the results and ensure that the document is properly closed:
		doc.save("results/pdfbox/Hello World-File.pdf");
		doc.close();
	}

	public static void main(String[] args) throws Exception {
		PDFbox pdfbox = new PDFbox();
//		pdfbox.test01();
		pdfbox.test02();
//		pdfbox.test03();
//		pdfbox.test04();
	}
}
