package org.oh.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.Test;
import org.oh.pdf.PdfUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class Postopia2 {
	protected List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	public Postopia2() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pgs_01", "848358L1030114602");
		map.put("pgs_02", "2011-01-01");
		map.put("pgs_03", "정성욱");
		map.put("pgs_04", "사무직");
		map.put("pgs_05", "011-3006-8816");
		map.put("pgs_06", "jsw@naver.com");
		map.put("pgs_07", "서울특별시 광진구");
		map.put("pgs_08", "1234");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("pgs_01", "848395L1030156583");
		map.put("pgs_02", "2012-02-02");
		map.put("pgs_03", "이경호");
		map.put("pgs_04", "공무원");
		map.put("pgs_05", "011-3006-8816");
		map.put("pgs_06", "jsw00@naver.com");
		map.put("pgs_07", "경기도 안양시 동안구");
		map.put("pgs_08", "1111");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("pgs_01", "8483424L103034283");
		map.put("pgs_02", "2014-05-06");
		map.put("pgs_03", "이경호");
		map.put("pgs_04", "");
		map.put("pgs_05", "017-3006-8816");
		map.put("pgs_06", "jsw01@naver.com");
		map.put("pgs_07", "충남 홍성군 홍성읍");
		map.put("pgs_08", "3425");
		list.add(map);
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
			Map<String, String> textMap = new HashMap<String, String>();

			// 필드 갯수
			for (Map<String, String> map : list) {
				for (Map.Entry<String, String> data : map.entrySet()) {
					textMap.put(data.getKey(), data.getValue());
				}

				PdfUtils.createPdf(textMap, "resources/pdf/다량 pdf 생성 test[템플릿]2.pdf",
						"results/pdf/postopia2/" + map.get("pgs_01") + ".pdf", map.get("pgs_08"));
			}
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
	}
}
