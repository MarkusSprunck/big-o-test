import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.internal.Reports;

public class SimpleTest {

	private static List<Integer> createSortInput(int size) {
		final List<Integer> result = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			result.add((int) Math.round(Integer.MAX_VALUE * Math.random()));
		}
		return result;
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
		for (int x = 64; x <= 32 * 1024; x *= 2) {
			sut.sort(createSortInput(x));
		}

		// ASSERT
		BigOAssert.assertLogLinear(bom, "sort");
		
		System.out.println("============================================");
		System.out.println("assertLogLinear_RunHeapSort_DetectLogLinear\n");
		System.out
				.println(Reports.createFullReport(bom.getResultTable("sort")));
	}

	@Test
	public void assertQuadratic_RunBubbleSort_DetectQuadratic() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final BubbleSort sut = (BubbleSort) bom.createProxy(BubbleSort.class);

		// ACT
		// Deactivate measurement to give JIT compiler the chance to optimize
		bom.deactivateMeasurement();
		sut.sort(createSortInput(128));
		// Activate measurement
		bom.activateMeasurement();
		for (int x = 64; x <= 2048; x *= 2) {
			sut.sort(createSortInput(x));
		}

		// ASSERT
		BigOAssert.assertQuadratic(bom, "sort");
		
		System.out.println("============================================");
		System.out.println("\nassertQuadratic_RunBubbleSort_DetectQuadratic\n");
		System.out.print(Reports.createFullReport(bom.getResultTable("sort")));
	}

}
