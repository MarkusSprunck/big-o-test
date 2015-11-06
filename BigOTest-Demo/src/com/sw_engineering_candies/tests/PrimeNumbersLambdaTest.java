package com.sw_engineering_candies.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOReports;
import com.sw_engineering_candies.big_o_test.interfaces.BigOParameter;

public class PrimeNumbersLambdaTest {

   static Logger LOGGER = Logger.getLogger(PrimeNumbersLambdaTest.class);

   /**
    * filters all prime numbers in a list of integer values and creates a string with all results
    */
   public String filterPrimeNumbersLambdaWrapper(@BigOParameter List<Integer> values) {
      return values.stream() //
            .parallel() //
            .filter(x -> isPrimeLambda(x)) //
            .map(Integer::valueOf) //
            .map(s -> s.toString()) //
            .collect(Collectors.joining(" "));
   }

   /**
    * prime number test
    */
   public static boolean isPrimeLambda(int n) {
      if (n <= 1) {
         return false;
      } else if (n == 2) {
         return true;
      }
      return n >= 2 && IntStream.rangeClosed(2, (int) (Math.sqrt(n))).allMatch((d) -> n % d != 0);
   }

   @Test
   public void filterPrimeNumbersLambdaWrapper_RunLambdaTest_DetectLinearDegree() {

      // ARRANGE
      final BigOAnalyser boa = new BigOAnalyser();
      final PrimeNumbersLambdaTest sut = (PrimeNumbersLambdaTest) boa.createProxy(PrimeNumbersLambdaTest.class);
      boa.deactivate();                                                 // measurement is
                                                                        // deactivated
      sut.filterPrimeNumbersLambdaWrapper(createSortInput(128 * 1024)); // give JIT compiler the
                                                                        // chance to optimize
      boa.activate();                                                   // measurement is active

      // ACT
      for (Integer x = (1024 * 1024); x >= 64; x /= 2) {
         final List<Integer> values = createSortInput(x);
         String result = sut.filterPrimeNumbersLambdaWrapper(values);
         LOGGER.debug("size=" + values.size() + " result=" + result);
      }

      final Table<Integer, String, Double> data = boa.getDataChecked("filterPrimeNumbersLambdaWrapper");
      System.out.println("--- PrimeNumbersLambdaTest -----------------------");
      System.out.println();
      System.out.println(BigOReports.getPolynomialDegree(data));
      System.out.println(BigOReports.getBestFunctionsReport(data));
      System.out.println(BigOReports.getDataReport(data));

      // ASSERT
      BigOAssert.assertLinear(boa, "filterPrimeNumbersLambdaWrapper");
   }

   private static List<Integer> createSortInput(Integer size) {
      final List<Integer> result = new ArrayList<Integer>(size);
      for (Integer i = 0; i < size; i++) {
         result.add(i);
      }
      return result;
   }

}
