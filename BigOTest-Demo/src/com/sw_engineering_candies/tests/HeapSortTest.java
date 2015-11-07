package com.sw_engineering_candies.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOReports;

public class HeapSortTest {

   @Test
   public void assertLogLinear_RunHeapSort_DetectLogLinear() {

      // ARRANGE
      final BigOAnalyser boa = new BigOAnalyser();
      final HeapSort sut = (HeapSort) boa.createProxy(HeapSort.class);
      boa.deactivate();                       // measurement is deactivated
      sut.sort(createSortInput(128 * 1024));  // give JIT compiler the chance to optimize
      boa.activate();                         // measurement is active

      // ACT
      for (int x = (128 * 1024); x >= 1024; x /= 2) {
         sut.sort(createSortInput(x));
      }
      traceReport(boa, "sort");

      // ASSERT
      BigOAssert.assertLogLinearOrPowerLaw(boa, "sort");

   }

   static List<Long> createSortInput(int size) {
      final List<Long> result = new ArrayList<Long>(size);
      for (int i = 0; i < size; i++) {
         result.add(Math.round(Long.MAX_VALUE * Math.random()));
      }
      return result;
   }

   private static void traceReport(final BigOAnalyser boa, String method) {
      System.out.println("--- HeapSortTest -----------------------");
      System.out.println();
      final Table<Integer, String, Double> data = boa.getDataChecked(method);
      System.out.println(BigOReports.getPolynomialDegree(data));
      System.out.println(BigOReports.getBestFunctionsReport(data));
      System.out.println(BigOReports.getDataReport(data));
   }

}
