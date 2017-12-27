package org.oh.pdf;

import java.util.concurrent.Future;

import org.junit.Test;
import org.oh.pdf.Image;
import org.oh.pdf.Page;
import org.oh.pdf.Pdf;
import org.oh.pdf.PdfUtils;
import org.oh.pdf.Text;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class Postopia3 {
	public Postopia3() {
	}

	@Test
	public void test() throws Exception {
		run(1, 1);
	}

	@Async
	public Future<Double> run(int threadNo, int count) throws Exception {
		StopWatch sw = new StopWatch();
		sw.start();

		// PDF 갯수
		for (int i = 1; i <= count; i++) {
			// 페이지 크기 (A4는 생략 가능)
//			Page.setSize(new int[] { 595, 842 });
			Pdf pdf = new Pdf("results/pdf/postopia3/postopia3" + (threadNo) + "-" + i);

			// 1 페이지
			Page page = new Page(1);
			pdf.addPage(page);

			// 이미지
			page.addImage(new Image("image1", "resources/img/postopia.jpg"));

			// 텍스트 (리눅스에서 폰트는 따로 복사해서 경로 지정)
			int default_font_size = 15;
//			page.addText(new Text("text1", "홍길동", 440, 290, 130, 20, Text.ALIGN_LEFT, Text.ALIGN_TOP, 0,
//					Base.COLOR_BLACK, "C:/windows/fonts/gulim.ttc,0", Text.FONT_ENCODING_IDENTITY_H,
//					Text.FONT_EMBEDDED, default_font_size, Text.FONT_STYLE_NORMAL, Base.COLOR_RED, 0.5f));
			page.addText(new Text("text1", "홍길동", 440, 293, 130, 20, "C:/windows/fonts/gulim.ttc,0",
					Text.FONT_ENCODING_IDENTITY_H, 12));

			int y = 316;
			page.addText(new Text("text2", "홍길동", 165, y, 400, 20, "C:/windows/fonts/gulim.ttc,0",
					Text.FONT_ENCODING_IDENTITY_H, default_font_size));
			y += 23;
			page.addText(new Text("text3", "홍길동", 165, y, 400, 20, "C:/windows/fonts/gulim.ttc,0",
					Text.FONT_ENCODING_IDENTITY_H, default_font_size));
			y += 23;
			page.addText(new Text("text4", "홍길동", 165, y, 400, 20, "C:/windows/fonts/gulim.ttc,0",
					Text.FONT_ENCODING_IDENTITY_H, default_font_size));

			y = 573;
			page.addText(new Text("text5", "홍길동", 165, y, 400, 20, "C:/windows/fonts/gulim.ttc,0",
					Text.FONT_ENCODING_IDENTITY_H, default_font_size));
			y += 23;
			page.addText(new Text("text6", "홍길동", 165, y, 400, 20, "C:/windows/fonts/gulim.ttc,0",
					Text.FONT_ENCODING_IDENTITY_H, default_font_size));
			y += 23;
			page.addText(new Text("text7", "홍길동", 165, y, 400, 20, "C:/windows/fonts/gulim.ttc,0",
					Text.FONT_ENCODING_IDENTITY_H, default_font_size));

			// PDF 생성
			PdfUtils.createPdf2(pdf, "1");
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
	}
}
