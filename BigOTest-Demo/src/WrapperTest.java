import java.util.Collections;
import java.util.List;
import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOParameter;

public class WrapperTest extends TestBase {

	public void sortWrapper(@BigOParameter List<Long> values) {
		Collections.sort(values);
	}

	@Test
	public void simpleSortWrapper_RunJavaCollections_DetectPowerLaw() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final WrapperTest sut = (WrapperTest) bom.createProxy(WrapperTest.class);
		bom.deactivate();                                                                                         // measurement is deactivated
		sut.sortWrapper(createSortInput(1024));        // give JIT compiler the chance to optimize
		bom.activate();                                                                                                // measurement is active

		// ACT
		for (int x = 256 * 1024; x >= 256; x /= 2) {
			sut.sortWrapper(createSortInput(x));
		}
		traceReport(bom, "sortWrapper", "WrapperTest\n");

		// ASSERT
		BigOAssert.assertPowerLaw(bom, "sortWrapper");
	}

}
