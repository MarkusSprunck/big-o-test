import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.internal.Reports;

public class BubbleSortTest {

	@Test
	public void assertQuadratic_RunBubbleSort_DetectQuadratic() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final BubbleSort sut = (BubbleSort) bom.createProxy(BubbleSort.class);

		// ACT
		// Deactivate measurement to give JIT compiler the chance to optimize
		bom.deactivateMeasurement();
		sut.sort(HeapSortTest.createSortInput(128));
		// Activate measurement
		bom.activateMeasurement();
		for (int x = 64; x <= 2048; x *= 2) {
			sut.sort(HeapSortTest.createSortInput(x));
		}
		System.out.println("assertQuadratic_RunBubbleSort_DetectQuadratic\n");
		HeapSortTest.traceReport(bom);

		// ASSERT
		BigOAssert.assertQuadratic(bom, "sort");
	}

}
