package org.oh.pdf;

import java.util.concurrent.Future;

import org.junit.Test;
import org.oh.pdf.Base;
import org.oh.pdf.Image;
import org.oh.pdf.Page;
import org.oh.pdf.Pdf;
import org.oh.pdf.PdfUtils;
import org.oh.pdf.Shape;
import org.oh.pdf.Text;
import org.oh.pdf.Shape.TYPE;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class Montessori {
	public Montessori() {
	}

	@Test
	public void test() throws Exception {
		run(1, 1);
	}

	@Async
	public Future<Double> run(int threadNo, int count) throws Exception {
		StopWatch sw = new StopWatch();
		sw.start();

		String fileName = "results/pdfbox/홈스쿨 위탁계약서";

		// PDF 갯수
		for (int i = 1; i <= count; i++) {
			// PDF
			Pdf pdf = new Pdf(fileName + "_" + (threadNo) + "-" + i, fileName + ".pdf");

			// 페이지
			Page page = new Page(1);
			pdf.addPage(page);

			// 텍스트
			page.addText(new Text("text1", "Hello World", 100, 100, 100, 12, Text.ALIGN_LEFT, Text.ALIGN_TOP,
					Base.COLOR_RED, 12, Text.FONT_STYLE_BOLD));

			// 이미지
			page.addImage(new Image("image1", "resources/pdfbox/header_logo.jpg", 200, 200, 145, 58));

			// 도형
			page.addShape(new Shape("shape1", 300, 300, 100, 100, Base.COLOR_BLUE, 0, 2, 0, TYPE.LINE));

			// PDF 생성
			PdfUtils.createPdf2(pdf);
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
	}
}
