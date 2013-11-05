import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOParameter;

public class WrapperTest extends TestBase {

	public Integer[] simpleWrapper(@BigOParameter List<Integer> input) {
		final SortedSet<Integer> sorted = new TreeSet<Integer>();
		sorted.addAll(input);
		Integer[] result = new Integer[sorted.size()];
		return sorted.toArray(result);
	}

	@Test
	public void assertLogLinear_RunJavaCollections_DetectLogLinear() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final WrapperTest sut = (WrapperTest) bom.createProxy(WrapperTest.class);
		bom.deactivate(); 											// measurement is deactivated
		sut.simpleWrapper(createSortInput(8192));				// give JIT compiler the chance to optimize
		bom.activate();												// measurement is active

		// ACT
		sut.simpleWrapper(createSortInput(8192));
		sut.simpleWrapper(createSortInput(4096));
		sut.simpleWrapper(createSortInput(2048));
		sut.simpleWrapper(createSortInput(1024));
		sut.simpleWrapper(createSortInput(512));

		// ASSERT
		BigOAssert.assertLogLinear(bom, "simpleWrapper");
		traceReport(bom, "simpleWrapper", "assertLogLinear_RunJavaCollections_DetectLogLinear\n");
	}

}
