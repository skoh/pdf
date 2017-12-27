package org.oh.pdf;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class ImageAlignmentPDF {
	public static void main(String[] args) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("results/pdf/ImageAlignmentPDF.pdf"));
			document.open();

			Image imageRight = Image.getInstance("resources/img/logo.jpg");
			imageRight.setBorder(Rectangle.BOX);
			imageRight.setBorderWidth(1f);
			imageRight.setAlignment(Image.RIGHT);

			Image imageCenter = Image.getInstance("resources/img/logo.jpg");
			imageCenter.setAlignment(Image.MIDDLE);

			Image imageLeft = Image.getInstance("resources/img/logo.jpg");
			imageLeft.setAlignment(Image.LEFT);

			document.add(imageRight);
			document.add(imageCenter);
			document.add(imageLeft);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
