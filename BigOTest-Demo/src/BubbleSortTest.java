// $codepro.audit.disable questionableName
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOReports;

public class BubbleSortTest {

   @Test
   public void assertQuadratic_RunBubbleSort_DetectQuadratic() {

      // ARRANGE
      final BigOAnalyser boa = new BigOAnalyser();
      final BubbleSort sut = (BubbleSort) boa.createProxy(BubbleSort.class);
      boa.deactivate();                       // measurement is deactivated
      sut.sort(createSortInput(1024));        // give JIT compiler the chance to optimize
      boa.activate();                         // measurement is active

      // ACT
      for (int x = 1024; x >= 16; x /= 2) {
         sut.sort(createSortInput(x));
      }
      traceReport(boa, "sort");

      // ASSERT
      BigOAssert.assertQuadratic(boa, "sort");
   }

   private static List<Long> createSortInput(int size) {
      final List<Long> result = new ArrayList<Long>(size);
      for (int i = 0; i < size; i++) {
         result.add(Math.round(Long.MAX_VALUE * Math.random()));
      }
      return result;
   }

   private static void traceReport(final BigOAnalyser boa, String method) {
      System.out.println("----------------------------------------");
      System.out.println("BubbleSortTest");
      final Table<Integer, String, Double> data = boa.getResultTableChecked(method);
      System.out.println(BigOReports.caclulateBestFunctionsTable(data));
      System.out.println(BigOReports.createDataReport(data));
   }

}
