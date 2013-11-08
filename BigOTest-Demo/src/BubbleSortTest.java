import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;

public class BubbleSortTest extends TestBase {

	@Test
	public void assertQuadratic_RunBubbleSort_DetectQuadratic() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final BubbleSort sut = (BubbleSort) bom.createProxy(BubbleSort.class);
		bom.deactivate();                                                                                         // measurement is deactivated
		sut.sort(createSortInput(1024));        // give JIT compiler the chance to optimize
		bom.activate();                                                                                                // measurement is active

		// ACT
		for (int x = 1024; x >= 16; x /= 2) {
			sut.sort(createSortInput(x));
		}
		traceReport(bom, "sort", "BubbleSortTest\n");

		// ASSERT
		BigOAssert.assertQuadratic(bom, "sort");
	}

}
