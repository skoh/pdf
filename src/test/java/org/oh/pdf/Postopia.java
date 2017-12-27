package org.oh.pdf;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.Test;
import org.oh.pdf.PdfUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class Postopia
//	implements Runnable
{
//	protected int threadNo;
//	protected int pdfNumber;

	public Postopia() {
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
//	1단계
//	1. PDF 건수               50 건
//	1) 쓰레드 갯수            10 개
//	- PDF 용량                 1 MB
//	평균제작속도            57.1 건/초
//	- PDF 용량                 3 MB
//	평균제작속도            37.9 건/초
//	- PDF 용량                 5 MB
//	평균제작속도            29.9 건/초
//
//	2. PDF 건수              100 건
//	- PDF 용량                 3 MB
//	평균제작속도            41.3 건/초
//
//	3. PDF 건수              500 건
//	- PDF 용량                 3 MB
//	평균제작속도            12.9 건/초
//
//	4. PDF 건수            1,000 건
//	- PDF 용량                 1 MB
//	평균제작속도            17.3 건/초
//	- PDF 용량                 3 MB
//	평균제작속도            12.9 건/초
//
//	<PDF 발송 속도 테스트 결과>
//	1. PDF 건수               50 건
//	- PDF 용량                 1 MB (MIME기준 약1.5 MB)
//	발송속도                 1.6 건/초
//
//	- PDF 용량                 5 MB (MIME기준 약7.0 MB)
//	발송속도                 0.3 건/초
	@Async
	public Future<Double> run(int threadNo, int count) throws Exception {
//	public double run(int threadNo, int count) {
		StopWatch sw = new StopWatch();
		sw.start();

		// PDF 갯수
		for (int i = 1; i <= count; i++) {
			Map<String, String> textMap = new HashMap<String, String>();

			// 증권
			textMap.put("Text01-01", "LZA3031****"); // 1
			textMap.put("Text01-02", "2014년 11월 06일");
			textMap.put("Text01-03", "김*");
			textMap.put("Text01-04", "640522-1******");
			textMap.put("Text01-05", "2014년 11월 06일 부터 2015년 11월 06일 24:00까지");

			textMap.put("Text02-01", "경기70노 66**");
			textMap.put("Text02-02", "KNHUP7513XS756590");
			textMap.put("Text02-03", "카니발(디젤)");
			textMap.put("Text02-04", "다인승비전방");
			textMap.put("Text02-05", "10"); // 10
			textMap.put("Text02-06", "기타수송용");
			textMap.put("Text02-07", "1999 년");
			textMap.put("Text02-08", "");
			textMap.put("Text02-09", "116만원");
			textMap.put("Text02-10", "");
			textMap.put("Text02-11", "15F등급 (55.5%)");
			textMap.put("Text02-12", "");

			// 청약서
			textMap.put("Text03-01", "851053L1106182613");
			textMap.put("Text03-02", "2014년 11월 06일 부터      2015년 11월 06일  24:00 까지");
			textMap.put("Text03-03", "김* ( 640522-1****** )"); // 20
			textMap.put("Text03-04", "김* ( 640522-1****** )");
			textMap.put("Text03-05", "내근회사원");
			textMap.put("Text03-06", "010 - 9065 - ****");
			textMap.put("Text03-07", "010 - 5326 - ****");
			textMap.put("Text03-08", "경기도 성남시 수정구*****************");
			textMap.put("Text03-09", "김종* ( 750805-1****** )");

			// 상품설명서
			textMap.put("Text04-01", "851053L1106182613");
			textMap.put("Text04-02", "MG손해보험(주)");
			textMap.put("Text04-03", "다이렉트사업부  서울3실     ( 박은진  1644-0114 )");
			textMap.put("Text04-04", "다이렉트개인용 자동차보험"); // 30
			textMap.put("Text04-05", "2014.11.06. 부터 2015.11.06. 까지");
			textMap.put("Text04-06", "경기70노66**  (카니발(디젤)  1999년식 )");
			textMap.put("Text04-07", "보험계약자 : 김*, 피보험자 : 김*");
			textMap.put("Text04-08", "일시납");
			textMap.put("Text04-09-01", "대인배상I               대인배상II             대물배상");
			textMap.put("Text04-09-02", "자기신체사고            무보험차상해           자기차량(기본)");
			textMap.put("Text04-10-01", "부부한정운전            만48세이상한정운전     조이카긴급출동I");
			textMap.put("Text04-10-02", "다른자동차수리비        신용카드"); // 38

			// 증권
			textMap.put("Text01-01", "LZA3031****"); // 1
			textMap.put("Text01-02", "2014년 11월 06일");
			textMap.put("Text01-03", "김*");
			textMap.put("Text01-04", "640522-1******");
			textMap.put("Text01-05", "2014년 11월 06일 부터 2015년 11월 06일 24:00까지");

			textMap.put("Text02-01", "경기70노 66**");
			textMap.put("Text02-02", "KNHUP7513XS756590");
			textMap.put("Text02-03", "카니발(디젤)");
			textMap.put("Text02-04", "다인승비전방");
			textMap.put("Text02-05", "10"); // 10
			textMap.put("Text02-06", "기타수송용");
			textMap.put("Text02-07", "1999 년");
			textMap.put("Text02-08", "");
			textMap.put("Text02-09", "116만원");
			textMap.put("Text02-10", "");
			textMap.put("Text02-11", "15F등급 (55.5%)");
			textMap.put("Text02-12", "");

			// 청약서
			textMap.put("Text03-01", "851053L1106182613");
			textMap.put("Text03-02", "2014년 11월 06일 부터      2015년 11월 06일  24:00 까지");
			textMap.put("Text03-03", "김* ( 640522-1****** )"); // 20
			textMap.put("Text03-04", "김* ( 640522-1****** )");
			textMap.put("Text03-05", "내근회사원");
			textMap.put("Text03-06", "010 - 9065 - ****");
			textMap.put("Text03-07", "010 - 5326 - ****");
			textMap.put("Text03-08", "경기도 성남시 수정구*****************");
			textMap.put("Text03-09", "김종* ( 750805-1****** )");

			// 상품설명서
			textMap.put("Text04-01", "851053L1106182613");
			textMap.put("Text04-02", "MG손해보험(주)");
			textMap.put("Text04-03", "다이렉트사업부  서울3실     ( 박은진  1644-0114 )");
			textMap.put("Text04-04", "다이렉트개인용 자동차보험"); // 30
			textMap.put("Text04-05", "2014.11.06. 부터 2015.11.06. 까지");
			textMap.put("Text04-06", "경기70노66**  (카니발(디젤)  1999년식 )");
			textMap.put("Text04-07", "보험계약자 : 김*, 피보험자 : 김*");
			textMap.put("Text04-08", "일시납");
			textMap.put("Text04-09-01", "대인배상I               대인배상II             대물배상");
			textMap.put("Text04-09-02", "자기신체사고            무보험차상해           자기차량(기본)");
			textMap.put("Text04-10-01", "부부한정운전            만48세이상한정운전     조이카긴급출동I");
			textMap.put("Text04-10-02", "다른자동차수리비        신용카드"); // 38

			// 증권
			textMap.put("Text01-01", "LZA3031****"); // 1
			textMap.put("Text01-02", "2014년 11월 06일");
			textMap.put("Text01-03", "김*");
			textMap.put("Text01-04", "640522-1******");
			textMap.put("Text01-05", "2014년 11월 06일 부터 2015년 11월 06일 24:00까지");

			textMap.put("Text02-01", "경기70노 66**");
			textMap.put("Text02-02", "KNHUP7513XS756590");
			textMap.put("Text02-03", "카니발(디젤)");
			textMap.put("Text02-04", "다인승비전방");
			textMap.put("Text02-05", "10"); // 10
			textMap.put("Text02-06", "기타수송용");
			textMap.put("Text02-07", "1999 년");
			textMap.put("Text02-08", "");
			textMap.put("Text02-09", "116만원");
			textMap.put("Text02-10", "");
			textMap.put("Text02-11", "15F등급 (55.5%)");
			textMap.put("Text02-12", "");

			// 청약서
			textMap.put("Text03-01", "851053L1106182613");
			textMap.put("Text03-02", "2014년 11월 06일 부터      2015년 11월 06일  24:00 까지");
			textMap.put("Text03-03", "김* ( 640522-1****** )"); // 20
			textMap.put("Text03-04", "김* ( 640522-1****** )");
			textMap.put("Text03-05", "내근회사원");
			textMap.put("Text03-06", "010 - 9065 - ****");
			textMap.put("Text03-07", "010 - 5326 - ****");

			String fileName = "results/pdf/postopia/다이렉트e패키지북1_결과" + (threadNo) + "-" + i;

			PdfUtils.createPdf(textMap, "resources/pdf/다이렉트e패키지북1.pdf", fileName + ".pdf", "1");
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
//		return sw.getTotalTimeSeconds();
	}
}
