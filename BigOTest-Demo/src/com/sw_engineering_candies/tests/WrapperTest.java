package com.sw_engineering_candies.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.interfaces.BigOParameter;

public class WrapperTest {

   public void sortWrapper(@BigOParameter List<Long> values) {
      Collections.sort(values);
   }

   @Test
   public void sortWrapper_RunJavaCollectionsSort_DetectPolynomialDegree() {

      // ARRANGE
      final BigOAnalyser boa = new BigOAnalyser();
      final WrapperTest sut = (WrapperTest) boa.createProxy(WrapperTest.class);
      boa.deactivate();                              // measurement is deactivated
      sut.sortWrapper(createSortInput(32 * 1024));   // give JIT compiler the chance to optimize
      boa.activate();                                // measurement is active

      // ACT
      for (int x = (32 * 1024); x >= 64; x /= 2) {
         sut.sortWrapper(createSortInput(x));
      }

      // ASSERT
      BigOAssert.assertPolynomialDegree(boa, "sortWrapper", 1.25, 0.5);
   }

   static List<Long> createSortInput(int size) {
      final List<Long> result = new ArrayList<Long>(size);
      for (int i = 0; i < size; i++) {
         result.add(Math.round(Long.MAX_VALUE * Math.random()));
      }
      return result;
   }

}
