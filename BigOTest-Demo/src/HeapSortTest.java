import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.internal.Reports;

public class HeapSortTest {

	public static List<Integer> createSortInput(int size) {
		final List<Integer> result = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			result.add((int) Math.round(Integer.MAX_VALUE * Math.random()));
		}
		return result;
	}

	public static void traceReport(final BigOAnalyser bom) {
		Table<Integer, String, Double> resultTable = bom.getResultTable("sort");
		System.out.println(Reports.createFullReport(resultTable));
	}

	@Test
	public void assertLogLinear_RunHeapSort_DetectLogLinear() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final HeapSort sut = (HeapSort) bom.createProxy(HeapSort.class);

		// ACT
		// Deactivate measurement to give JIT compiler the chance to optimize
		bom.deactivateMeasurement();
		sut.sort(createSortInput(128));
		// Activate measurement
		bom.activateMeasurement();
		for (int x = 128; x <= 32 * 1024; x *= 2) {
			sut.sort(createSortInput(x));
		}
		System.out.println("assertLogLinear_RunHeapSort_DetectLogLinear\n");
		traceReport(bom);

		// ASSERT
		BigOAssert.assertLogLinear(bom, "sort");

	}

}
