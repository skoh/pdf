package org.oh.pdf;

import java.util.concurrent.Future;

import org.oh.pdf.Pdf;
import org.oh.pdf.PdfUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class Html {
	public Html() {
	}

//	3단계
//	1. PDF 건수            1,000 건
//	1) 쓰레드 갯수            10 개
//
//	1. PDF 용량               71 KB (이미지 없음)
//	평균제작속도            23.2 건/초
//
//	2. PDF 용량              206 KB (이미지 있음)
//	평균제작속도            21.1 건/초
//
//	3. PDF 용량            1,150 KB (이미지 있음)
//	평균제작속도            14.1 건/초
	@Async
	public Future<Double> run(int threadNo, int count) {
		StopWatch sw = new StopWatch();
		sw.start();

		// PDF 갯수
		for (int i = 1; i <= count; i++) {
			Pdf pdf = new Pdf("results/pdf/html/layout_school_" + (threadNo) + "-" + i,
					"resources/xml/layout_school3.html");

			PdfUtils.convertHtmlToPdf(pdf, "1");
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
	}
}
