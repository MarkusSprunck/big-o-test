import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;

public class SimpleTest {

	private static List<Integer> createSortInput(int size) {
		final List<Integer> result = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			result.add((int) Math.round(100.0f * Math.random()));
		}
		return result;
	}

	@Test
	public void assertLogLinear_RunHeapSort_DetectLogLinear() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final HeapSort sut = (HeapSort) bom.createProxy(HeapSort.class);

		// ACT
		sut.sort(createSortInput(300000));
		sut.sort(createSortInput(100000));
		sut.sort(createSortInput(30000));
		sut.sort(createSortInput(10000));
		sut.sort(createSortInput(3000));
		sut.sort(createSortInput(1000));

		// ASSERT
		BigOAssert.assertLogLinear(bom, "sort");
	}

}
