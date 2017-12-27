package org.oh.pdf;

import java.util.concurrent.Future;

import org.junit.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class Test01 {
	public Test01() {
	}

	@Test
	public void test() throws Exception {
		run(1, 1);
	}

	@Async
	public Future<Double> run(int threadNo, int count) throws Exception {
		StopWatch sw = new StopWatch();
		sw.start();

		for (int i = 1; i <= count; i++) {
			System.out.println(threadNo + "(" + Thread.currentThread().getName() + ")-" + i);
			Thread.sleep(1000);
		}

		sw.stop();
		return new AsyncResult<Double>(sw.getTotalTimeSeconds());
	}
}
