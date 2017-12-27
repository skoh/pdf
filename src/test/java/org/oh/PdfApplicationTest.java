package org.oh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oh.PdfApplication;
import org.oh.pdf.Html;
import org.oh.pdf.Jasper;
import org.oh.pdf.KT;
import org.oh.pdf.Montessori;
import org.oh.pdf.PDFbox;
import org.oh.pdf.Postopia;
import org.oh.pdf.Postopia1;
import org.oh.pdf.Postopia2;
import org.oh.pdf.Postopia3;
import org.oh.pdf.Test01;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

/**
 * <pre>
 * 성능 테스트
 * 주의) 실행시 PdfApplication 의 @SpringBootApplication 을 주석 처리
 * </pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PdfApplication.class)
@Configuration
@EnableAsync
//@ComponentScan(basePackages = { "org.oh.pdf" })
@ContextConfiguration("classpath:spring-config_test.xml")
public class PdfApplicationTest {
	@Autowired
	protected Test01 test01;
	@Autowired
	protected Postopia postopia;
	@Autowired
	protected Postopia1 postopia1;
	@Autowired
	protected Postopia2 postopia2;
	@Autowired
	protected Postopia3 postopia3;
	@Autowired
	protected KT kt;
	@Autowired
	protected Html html;
	@Autowired
	protected Jasper jasper;
	@Autowired
	protected PDFbox pdfbox;
	@Autowired
	protected Montessori montessori;

	@Autowired
	protected ExecutorConfigurationSupport executor;

	@BeforeClass
	public static void beforeClass() throws Exception {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.reset();

		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(context);
		configurator.doConfigure("conf/logback.xml");
	}

	@Test
	public void test() throws Exception {
		int pdfNumber = 1; // 쓰레드당 PDF 갯수
		int threadNumber = 3; // 쓰레드 갯수
		int repetitionNumber = 1; // 반복 횟수

		double seconds = 0; // 쓰레드별 소요시간
		double subAvg = 0; // 회별 평균소요시간
		double average = 0; // 전체 평균소요시간

		System.out.println("1234567890123456789123456789123456789");
		System.out.println(String.format("%-15s %,10d 건", "1. PDF 건수", (pdfNumber * threadNumber)));
		System.out.println(String.format("%-12s %,10d 개", "1) 쓰레드 갯수", threadNumber));

		for (int i = 1; i <= repetitionNumber; i++) {
			List<Future<Double>> list = new ArrayList<Future<Double>>();

			for (int j = 1; j <= threadNumber; j++) {
				// 쓰레드당 PDF 갯수
				Future<Double> future = test01.run(j, pdfNumber);
				list.add(future);
//				Thread t = new Thread(new Postopia(j, pdfNumber));
//				t.start();
			}
			try {
				for (int j = 0; j < list.size(); j++) {
					seconds = list.get(j).get();
//					System.out.println(String.format("%-13s %,10.1f 초", (j + 1) + "번 쓰레드", seconds));
					subAvg += seconds;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			seconds = subAvg / threadNumber;
			System.out.println(String.format("%-10s %,10.1f 초", i + "회 평균소요시간", seconds));
			System.out
					.println(String.format("%-10s %,10.1f 건/초", i + "회 평균제작속도", (pdfNumber * threadNumber) / seconds));
			average += subAvg;
			subAvg = 0;
		}

		seconds = average / (threadNumber * repetitionNumber);
		System.out.println(String.format("%-11s %,10.1f 초", "평균소요시간", seconds));
		System.out.println(String.format("%-11s %,10.1f 건/초", "평균제작속도", (pdfNumber * threadNumber) / seconds));
		System.out.println("----------------------------------------");

		executor.shutdown();

//		try {
//			Thread.sleep(1000 * 1000);
//		} catch (Exception e) {
//			e.printStackTrace(); 
//		}
	}
}
