import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;

public class HeapSortTest extends TestBase {

   private static final String NL = System.getProperty("line.separator");

   @Test
   public void assertLogLinear_RunHeapSort_DetectLogLinear() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final HeapSort sut = (HeapSort) bom.createProxy(HeapSort.class);
      bom.deactivate();                                                                                         // measurement is deactivated
      sut.sort(createSortInput(1024));        // give JIT compiler the chance to optimize
      bom.activate();                                                                                                // measurement is active

      // ACT
      for (int x = (4 * 1024); x >= 16; x /= 2) {
         sut.sort(createSortInput(x));
      }
      traceReport(bom, "sort", "HeapSortTest".concat(NL));

      // ASSERT
      BigOAssert.assertLogLinearOrPowerLaw(bom, "sort");

   }

}
