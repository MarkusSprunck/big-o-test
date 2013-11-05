import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOParameter;


public class WrapperTest {

	public Integer[] simpleSortWrapper(@BigOParameter List<Integer> input) {
		final SortedSet<Integer> sorted = new TreeSet<Integer>();
		sorted.addAll(input);
		Integer[] result = new Integer[sorted.size()];
		return sorted.toArray(result);
	}

	private static List<Integer> createSortInput(int size) {
		final List<Integer> result = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			result.add((int) Math.round(100000 * Math.random()));
		}
		return result;
	}

	@Test
	public void assertLogLinear_RunJavaCollections_DetectLogLinear() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final WrapperTest sut = (WrapperTest) bom.createProxy(WrapperTest.class);
		// Deactivate measurement to give JIT compiler the chance to optimize
		bom.deactivateMeasurement();
		sut.simpleSortWrapper(createSortInput(128));
		// Activate measurement
		bom.activateMeasurement();

		// ACT
		int x = 4*1024;
		do {
			sut.simpleSortWrapper(createSortInput(x));
			x /= 2;
		} while (x >= 128);

		// ASSERT
		BigOAssert.assertLogLinear(bom, "simpleSortWrapper");
	}

}
