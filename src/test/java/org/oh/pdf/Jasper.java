package org.oh.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import org.oh.pdf.JRPdfExporter2;
import org.oh.pdf.PdfUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.itextpdf.text.pdf.PdfWriter;

@Component
public class Jasper {
//	4단계
//	1. PDF 건수            1,000 건
//	1) 쓰레드 갯수            10 개
//
//	- PDF 용량                75 KB (이미지 없음)	
//	평균제작속도           106.2 건/초
	@Async
	public Future<Double> run(int threadNo, int count) throws Exception {
		StopWatch sw = new StopWatch();
		sw.start();

		// JasperReport 객체 생성
		JasperReport jasperReport = JasperCompileManager.compileReport("resources/xml/Blank_A4.jrxml");
//		JasperCompileManager.compileReportToFile("resources/xml/Blank_A4.jrxml", "resources/xml/Blank_A4.jasper");
//		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("resources/xml/Blank_A4.jasper");

		// 파라미터 설정
		Map<String, Object> parameters = new HashMap<String, Object>();

		// 1. Parameter > String 추가
		parameters.put("Parameter1", "파라미터1");

		// 2-1. Main DataSource(1개) > List 추가
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 1; i <= 20; i++)
			list.add(new Object[] { "회원" + i });
		parameters.put(PdfUtils.MAIN_DATA_SOURCE, list);
		PdfUtils.addParameterColumnNames(PdfUtils.MAIN_DATA_SOURCE, new String[] { "USERNAME" }, parameters);

		// 3-1. Sub DataSource(n개) > List 추가
		list = new ArrayList<Object[]>();
		list.add(new Object[] { "회원3", 1000 });
		list.add(new Object[] { "회원4", 2000 });
		parameters.put("DataSource1", list);
		PdfUtils.addParameterColumnNames("DataSource1", new String[] { "USERID", "POINT" }, parameters);

		list = new ArrayList<Object[]>();
		list.add(new Object[] { "회원5", 3000 });
		list.add(new Object[] { "회원6", 4000 });
		parameters.put("DataSource2", list);
		PdfUtils.addParameterColumnNames("DataSource2", new String[] { "USERID", "POINT" }, parameters);

		// PDF 갯수
		for (int i = 1; i <= count; i++) {
			Map<String, Object> jasperParameters = PdfUtils.convertJasperParameters(parameters);

			// 4. PDF 변환
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameters); // , new JREmptyDataSource())); // conn

			JRPdfExporter2 exporter = new JRPdfExporter2();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("results/pdf/jasper/Blank_A4" + (threadNo)
					+ "-" + i + ".pdf"));

			// 5. 암호화
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.isCreatingBatchModeBookmarks();
			configuration.setEncrypted(true);
			configuration.set128BitKey(true);
			configuration.setUserPassword("1");
			configuration.setOwnerPassword("1");
			configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
			exporter.setConfiguration(configuration);

			exporter.exportReport();
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
	}
}
