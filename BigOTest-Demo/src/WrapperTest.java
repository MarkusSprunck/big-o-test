import java.util.Collections;
import java.util.List;
import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOParameter;

public class WrapperTest extends TestBase {

	public List<Long> simpleSortWrapper(@BigOParameter List<Long> values) {
		Collections.sort(values);
		return values;
	}

	@Test
	public void simpleSortWrapper_RunJavaCollections_DetectPowerLaw() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final WrapperTest sut = (WrapperTest) bom.createProxy(WrapperTest.class);
		bom.deactivate();                                                                                         // measurement is deactivated
		sut.simpleSortWrapper(createSortInput(1024));        // give JIT compiler the chance to optimize
		bom.activate();                                                                                                // measurement is active

		// ACT
		for (int x = 1024 * 1024; x >= 1024; x /= 2) {
			sut.simpleSortWrapper(createSortInput(x));
		}
		traceReport(bom, "simpleSortWrapper", "WrapperTest\n");

		// ASSERT
		BigOAssert.assertPowerLaw(bom, "simpleSortWrapper");
	}

}
