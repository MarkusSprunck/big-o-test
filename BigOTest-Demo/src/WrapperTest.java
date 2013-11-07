import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOParameter;

public class WrapperTest extends TestBase {

	public Long[] simpleWrapperFirst(@BigOParameter List<Long> input) {
		final SortedSet<Long> sorted = new TreeSet<Long>();
		sorted.addAll(input);

		Long[] result = new Long[sorted.size()];
		return sorted.toArray(result);
	}

	public Long[] simpleWrapperSecond(@BigOParameter List<Long> input) {
		final List<Long> sorted = new ArrayList<Long>();
		sorted.addAll(input);
		Collections.sort(sorted);

		Long[] result = new Long[sorted.size()];
		return sorted.toArray(result);
	}

	@Test
	public void simpleWrapperFirst_RunJavaCollections_DetectPowerLaw() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final WrapperTest sut = (WrapperTest) bom.createProxy(WrapperTest.class);
		bom.deactivate(); 											// measurement is deactivated
		sut.simpleWrapperFirst(createSortInput(16384));		// give JIT compiler the chance to optimize
		bom.activate();												// measurement is active

		// ACT
		for (int x = 4 * 65536; x >= 512; x /= 2) {
			sut.simpleWrapperFirst(createSortInput(x));
		}
		traceReport(bom, "simpleWrapperFirst", "simpleWrapperFirst_RunJavaCollections_DetectPowerLaw\n");

		// ASSERT
		BigOAssert.assertPowerLaw(bom, "simpleWrapperFirst");
	}

	@Test
	public void simpleWrapperSecond_RunJavaCollections_DetectPowerLaw() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final WrapperTest sut = (WrapperTest) bom.createProxy(WrapperTest.class);
		bom.deactivate(); 											// measurement is deactivated
		sut.simpleWrapperSecond(createSortInput(16384));	// give JIT compiler the chance to optimize
		bom.activate();												// measurement is active

		// ACT
		for (int x = 4 * 65536; x >= 512; x /= 2) {
			sut.simpleWrapperSecond(createSortInput(x));
		}
		traceReport(bom, "simpleWrapperSecond", "simpleWrapperSecond_RunJavaCollections_DetectPowerLaw\n");

		// ASSERT
		BigOAssert.assertPowerLaw(bom, "simpleWrapperSecond");
	}

}
