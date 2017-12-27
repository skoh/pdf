package org.oh.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

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
public class KT {
	public KT() {
	}

	@Async
	public Future<Double> run(int threadNo, int count) {
		StopWatch sw = new StopWatch();
		sw.start();

		// PDF 갯수
		for (int i = 1; i <= count; i++) {
			// 필드 갯수
			List<Page> pages = new ArrayList<Page>();
			Pdf pdf = new Pdf("results/pdf/kt/2015년 01월 olleh email 명세서(010-32xx-x794)" + (threadNo) + "-" + i, pages);

			for (int j = 1; j <= 4; j++) {
				List<Text> texts = new ArrayList<Text>();
//				for (int k = 0; k < 12; k++) {
				for (int k = 0; k < 5; k++) {
//				for (int k = 0; k < 3; k++) {
					Text text = new Text("" + j + (k + 1), "2014년 11월 06일 부터 2015년 11월 06일 24:00까지", 100,
							100 + (k * 30), 500, 12, Text.ALIGN_LEFT, Text.ALIGN_TOP, 12);
					texts.add(text);
				}

				List<Image> images = new ArrayList<Image>();
				Image image = new Image("image1",
						"C:/skoh/mnwise/wiseU/개선사항/pdf저작기/KT/images/2015년 01월 olleh email 명세서(010-32xx-x794)_"
								+ String.format("%02d", j) + ".jpg", 909, 1300);
				images.add(image);

				Page page = new Page(j, images, texts);
				pages.add(page);
			}

			PdfUtils.createPdf2(pdf, "1");
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
	}
}
