package org.oh.pdf;

import java.util.ArrayList;
import java.util.List;
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
public class Postopia1
//	implements Runnable
{
//	protected int threadNo;
//	protected int pdfNumber;

	public Postopia1() {
	}

	@Test
	public void test() throws Exception {
		run(1, 1);
	}

//	public Postopia(int threadNo, int pdfNumber) {
//		super();
//		this.threadNo = threadNo;
//		this.pdfNumber = pdfNumber;
//	}
//
//	public void run() {
//		try {
//			System.out.println(String.format("%-13s %,10.1f 초", (this.threadNo) + "번 쓰레드", run(threadNo, pdfNumber)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	<테스트 환경>
//	1) 방식: PDF양식, 이미지 파일
//	2) 횟수: 3회
//	3) 건수: 50,100,500,1000 건/회
//	4) 용량: 1,3,5 MByte/건
//	5) 페이지: 8,20,34 페이지/건
//	6) 필드: 100 개/건
//	7) 128 bit 암호화
//	8) 시스템 사양
//	  - 프로세서 : Intel(R) Core(TM) i7-4702MQ CPU @ 2.2OGHz 2.19GHz
//	  - 설치된 메모리(RAM) : 8.00G8
//	  - 시스템 종류 : 윈도우즈 64비트 운영체제, x64 기반 프로새서
//
//	<PDF 제작 속도 테스트 결과>
//	2단계
//	1. PDF 건수               50 건
//	1) 쓰레드 갯수            10 개
//	- PDF 용량                 1 MB
//	평균제작속도            91.4 건/초
//	- PDF 용량                 3 MB
//	평균제작속도            44.5 건/초
//	- PDF 용량                 5 MB
//	평균제작속도            31.5 건/초
//	- PDF 용량                 3 MB
//
//	2. PDF 건수              100 건
//	- PDF 용량                 3 MB
//	평균제작속도            47.7 건/초
//
//	3. PDF 건수              500 건
//	- PDF 용량                 3 MB
//	평균제작속도            14.2 건/초
//
//	4 PDF 건수             1,000 건
//	- PDF 용량                 1 MB
//	평균제작속도            22.6 건/초
//	- PDF 용량                 3 MB
//	평균제작속도            12.6 건/초
	@Async
	public Future<Double> run(int threadNo, int count) throws Exception {
		StopWatch sw = new StopWatch();
		sw.start();

		// PDF 갯수
		for (int i = 1; i <= count; i++) {
			List<Page> pages = new ArrayList<Page>();
			Pdf pdf = new Pdf("results/pdf/postopia1/다이렉트e패키지북1_결과" + (threadNo) + "-" + i, pages);

			// 필드 갯수
			for (int j = 1; j <= 6; j++) { // 1 MB
//			for (int j = 1; j <= 20; j++) { // 3 MB
//			for (int j = 1; j <= 32; j++) { // 5 MB
				List<Text> texts = new ArrayList<Text>();
				for (int k = 0; k < 12; k++) { // 100 개 필드
//				for (int k = 0; k < 5; k++) {
//				for (int k = 0; k < 3; k++) {
					Text text = new Text("" + j + (k + 1), "2014년 11월 06일 부터 2015년 11월 06일 24:00까지", 100,
							100 + (k * 30), 500, 12, Text.ALIGN_LEFT, Text.ALIGN_TOP, 12);
					texts.add(text);
				}

				List<Image> images = new ArrayList<Image>();
				Image image = new Image("image1", "resources/img/다이렉트e패키지북_페이지_" + String.format("%02d", j) + ".jpg",
						595, 842);
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
