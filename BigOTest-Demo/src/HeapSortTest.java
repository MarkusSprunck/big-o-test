import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;

public class HeapSortTest extends TestBase {

	@Test
	public void assertLogLinear_RunHeapSort_DetectLogLinear() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final HeapSort sut = (HeapSort) bom.createProxy(HeapSort.class);
		bom.deactivate(); 											// measurement is deactivated
		sut.sort(createSortInput(8192));							// give JIT compiler the chance to optimize
		bom.activate();												// measurement is active

		// ACT
		sut.sort(createSortInput(8192));
		sut.sort(createSortInput(4096));
		sut.sort(createSortInput(2048));
		sut.sort(createSortInput(1024));

		// ASSERT
		BigOAssert.assertLogLinear(bom, "sort");
		traceReport(bom, "sort", "assertLogLinear_RunHeapSort_DetectLogLinear\n");

	}

}
