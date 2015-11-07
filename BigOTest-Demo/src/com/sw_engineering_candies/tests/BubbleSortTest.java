package com.sw_engineering_candies.tests;

import static org.junit.Assert.assertEquals;

// $codepro.audit.disable questionableName
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class BubbleSortTest {

   static List<Long> createSortInput(int size) {
      final List<Long> result = new ArrayList<Long>(size);
      for (int i = 0; i < size; i++) {
         result.add(Math.round(1000 * Math.random()));
      }
      return result;
   }

   @Test
   public void bubbleSort_SmallList_SortedAscending() {

      // ARRANGE
      final List<Long> values = BubbleSortTest.createSortInput(100);
      List<Long> expected = new ArrayList<Long>(values);
      Collections.sort(expected);

      // ACT
      final List<Long> actual = Arrays.asList(new BubbleSort().sort(values));

      // ASSERT
      assertEquals(expected, actual);
   }

}
