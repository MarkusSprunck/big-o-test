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
      final BigOAnalyser bom = new BigOAnalyser();
      final BubbleSort sut = (BubbleSort) bom.createProxy(BubbleSort.class);
      bom.deactivate();                                                                                         // measurement is deactivated
      sut.sort(createSortInput(1024));        // give JIT compiler the chance to optimize
      bom.activate();                                                                                                // measurement is active

      // ACT
      for (int x = 1024; x >= 16; x /= 2) {
         sut.sort(createSortInput(x));
      }
      traceReport(bom, "sort");

      // ASSERT
      BigOAssert.assertQuadratic(bom, "sort");
   }

   private static List<Long> createSortInput(int size) {
      final List<Long> result = new ArrayList<Long>(size);
      for (int i = 0; i < size; i++) {
         result.add(Math.round(Long.MAX_VALUE * Math.random()));
      }
      return result;
   }

   private static void traceReport(final BigOAnalyser bom, String method) {
      System.out.println("----------------------------------------");
      System.out.println("BubbleSortTest");
      final Table<Integer, String, Double> resultTable = bom.getResultTableChecked(method);
      System.out.println(BigOReports.caclulateBestFunctionsTable(resultTable));
      System.out.println(BigOReports.createDataReport(resultTable));
   }

}
