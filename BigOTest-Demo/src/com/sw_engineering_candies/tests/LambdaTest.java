package com.sw_engineering_candies.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOResult;

public class LambdaTest {

   @Test
   public void bubbleSortDetectQuadratic() {

      // ARRANGE
      List<List<Long>> values = LongStream.range(6, 14) //
            .mapToInt(i -> 1 << i) //
            .mapToObj(x -> BubbleSortTest.createSortInput(x)) //
            .collect(Collectors.toList());

      // ACT
      BigOResult actual = BigOAnalyser.classUnderTest(BubbleSort.class) //
            .execute((BubbleSort o) -> values.stream().forEach(value -> o.sort(value))) //
            .trace();

      // ASSERT
      BigOAssert.assertQuadratic(actual.getBigOAnalyser(), "sort");
   }

   @Test
   public void heapSortDetectLogLinear() {

      // ARRANGE
      List<List<Long>> values = LongStream.range(6, 14) //
            .mapToInt(i -> 1 << i) //
            .mapToObj(x -> HeapSortTest.createSortInput(x)) //
            .collect(Collectors.toList());

      // ACT
      BigOResult actual = BigOAnalyser.classUnderTest(HeapSort.class) //
            .execute((HeapSort o) -> values.stream().forEach(value -> o.sort(value))) //
            .trace();

      // ASSERT
      BigOAssert.assertLogLinearOrPowerLaw(actual.getBigOAnalyser(), "sort");
   }

   @Test
   public void sortWrapperDetectPolynomialDegree() {

      // ARRANGE
      List<List<Long>> values = LongStream.range(6, 16) //
            .mapToInt(i -> 1 << i) //
            .mapToObj(x -> WrapperTest.createSortInput(x)) //
            .collect(Collectors.toList());

      // ACT
      BigOResult actual = BigOAnalyser.classUnderTest(WrapperTest.class) //
            .execute((WrapperTest o) -> values.stream().forEach(value -> o.sortWrapper(value))) //
            .trace();

      // ASSERT
      BigOAssert.assertPolynomialDegree(actual.getBigOAnalyser(), "sortWrapper", 1.10, 0.2);
   }

}
