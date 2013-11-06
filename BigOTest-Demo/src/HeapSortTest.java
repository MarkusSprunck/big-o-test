import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;

public class HeapSortTest extends TestBase {

	@Test
	public void assertLogLinear_RunHeapSort_DetectPowerLaw() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final HeapSort sut = (HeapSort) bom.createProxy(HeapSort.class);
		bom.deactivate(); 											// measurement is deactivated
		sut.sort(createSortInput(8192));							// give JIT compiler the chance to optimize
		bom.activate();												// measurement is active

		// ACT
		for (int x = 4 * 65536; x >= 128; x /= 2) {
			sut.sort(createSortInput(x));
		}

		// ASSERT
		BigOAssert.assertPowerLaw(bom, "sort");
		traceReport(bom, "sort", "assertLogLinear_RunHeapSort_DetectPowerLaw\n");

	}

}
