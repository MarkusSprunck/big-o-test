/*
 * Copyright (C) 2013, Markus Sprunck <sprunck.markus@gmail.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.sw_engineering_candies.big_o_test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.fitter.Item;
import com.sw_engineering_candies.big_o_test.utils.Algorithms;

public class BigOAnalyserTest {

   private static final String NL = System.getProperty("line.separator");

   final BigOAnalyser bom = new BigOAnalyser();

   @Test
   public void getResultTable_wrongMethodName_GetIllegalStateException() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runQuadratic(10);

      // ACT
      boolean exceptionHappened = false;
      try {
         bom.getResultTable("wrongMethodName");
      } catch (final IllegalStateException ex) {
         System.out.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void run_RunCalled_CorrectResult() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input = { 11, 22, 33, 44 };
      final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };

      // ACT
      final double result = sut.run(m_input, true, n_input, k_input);

      // ASSERT
      Assert.assertEquals(117583.3969669342, result);
   }

   @Test
   public void getValue_OneCall_GetNamoTime() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input = { 11, 22, 33, 44 };
      final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input, true, n_input, k_input);
      sut.run(m_input, true, n_input, k_input);

      // ACT
      final Item result = bom.getValue("run#8#4#5");
      final long actual = result.getTime();

      // ASSERT
      Assert.assertTrue(actual > 0L);
   }

   @Test
   public void getValue_OneCall_GetNumberOfCallsOne() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input = { 11, 22, 33, 44 };
      final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input, true, n_input, k_input);

      // ACT
      final Item result = bom.getValue("run#8#4#5");
      result.setCalls(1);

      // ASSERT
      Assert.assertEquals(1, result.getCalls());
   }

   @Test
   public void getValuel_OneCallWithWrongKey_RetrunsNull() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input = { 11, 22, 33, 44 };
      final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input, true, n_input, k_input);

      // ACT
      final Item result = bom.getValue("ThisIsWrongKey#8#4#5");

      // ASSERT
      Assert.assertNull(result);
   }

   @Test
   public void getKeys_ThreeCallsWithDifferentValues_GetCorrectKeys() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

      final List<Integer> m_input1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input1 = { 11, 22, 33, 44 };
      final float[] k_input1 = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input1, true, n_input1, k_input1);
      sut.run(m_input1, true, n_input1, k_input1);

      final List<Integer> m_input2 = Arrays.asList(1, 4, 5, 6, 7, 10);
      final int[] n_input2 = { 11, 22, 33 };
      final float[] k_input2 = { 11.4f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input2, true, n_input2, k_input2);

      final List<Integer> m_input3 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
      final int[] n_input3 = { 11 };
      final float[] k_input3 = { 11.4f, 2.23f };
      sut.run(m_input3, true, n_input3, k_input3);

      // ACT
      final Set<String> actual = bom.getKeys();

      // ASSERT
      final Set<String> expectedKeys = new HashSet<String>(10);
      expectedKeys.add("run#8#4#5");
      expectedKeys.add("run#6#3#4");
      expectedKeys.add("run#10#1#2");
      Assert.assertEquals(expectedKeys, actual);
   }

   @Test
   public void estimatePolynomialDegree_MannyCallsOfRunLinear_GetDegreeOfPolynomIsThree() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      for (int n = 1; n <= 128; n *= 2) {
         sut.runLinear((n));
         // replace measured results
         final Item result = bom.getValue("runLinear#" + n);
         result.setNanoTime(n * n * n * 134 + n * n * +n + 11);
         result.setCalls(1);
      }
      final Table<Integer, String, Double> report = bom.getResultTable("runLinear");

      // ACT
      final long result = Math.round(BigOAssert.estimatePolynomialDegree(report));

      // ASSERT
      Assert.assertEquals(3L, result);
   }

   @Test
   public void estimatePolynomialDegree_MannyCallsOfRunLinear_GetDegreeOfPolynomIsOne() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      for (int n = 1; n <= 128; n *= 2) {
         sut.runLinear((n));
         // replace measured results
         final Item result = bom.getValue("runLinear#" + n);
         result.setNanoTime(n * 134 + 11);
         result.setCalls(1);
      }
      final Table<Integer, String, Double> report = bom.getResultTable("runLinear");

      // ACT
      final long result = Math.round(BigOAssert.estimatePolynomialDegree(report));

      // ASSERT
      Assert.assertEquals(1L, result);
   }

   @Test
   public void estimatePolynomialDegree_MannyCallsOfrunQuadratic_GetDegreeOfPolynomIsTwo() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      for (int n = 1; n <= 128; n *= 2) {
         sut.runQuadratic((n));
         // replace measured results
         final Item result = bom.getValue("runQuadratic#" + n);
         result.setNanoTime(n * n * 123 + n + 1);
         result.setCalls(1);
      }
      final Table<Integer, String, Double> report = bom.getResultTable("runQuadratic");

      // ACT
      final long result = Math.round(BigOAssert.estimatePolynomialDegree(report));

      // ASSERT
      Assert.assertEquals(2L, result);
   }

   @Test
   public void estimatePolynomialDegree_MannyCallsOfrunConst_GetDegreeOfPolynomIsZero() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      for (int n = 1; n <= 128; n *= 2) {
         sut.runConstant(n);
      }
      final Table<Integer, String, Double> report = bom.getResultTable("runConstant");

      // ACT
      final long result = Math.round(BigOAssert.estimatePolynomialDegree(report));

      // ASSERT
      Assert.assertEquals(0L, result);
   }

   @Test
   public void getResultTable_TwoCalls_GetTableWithAllValues() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

      final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input = { 11, 22, 33, 44 };
      final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input, true, n_input, k_input);
      final Item result = bom.getValue("run#8#4#5");
      result.setNanoTime(12345);
      result.setCalls(1);

      final List<Integer> m_input2 = Arrays.asList(1, 2, 3, 4, 5, 6);
      final int[] n_input2 = { 11, 22, 33, 44, 55, 66, 77, 88 };
      final float[] k_input2 = {};
      sut.run(m_input2, true, n_input2, k_input2);
      final Item result2 = bom.getValue("run#6#8#0");
      result2.setNanoTime(23456);
      result2.setCalls(1);

      // ACT
      final String actual = bom.getResultTable("run").toString();

      // ASSERT
      final String expected = "{1={N1=8.0, N2=4.0, N3=5.0, TIME=12345.0}, 2={N1=6.0, N2=8.0, N3=0.0, TIME=23456.0}}";
      Assert.assertEquals(expected, actual);
   }

   @Test
   public void getResultTable_MixedCallsOfTwoFunctions_GetTableforDifferentMethodNames() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input = { 11, 22, 33, 44 };
      final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input, true, n_input, k_input);
      final Item result = bom.getValue("run#8#4#5");
      result.setNanoTime(12345);
      result.setCalls(1);

      final List<Integer> m_input2 = Arrays.asList(1, 2, 3, 4, 5, 6);
      final int[] n_input2 = { 11, 22, 33, 44, 55, 66, 77, 88 };
      final float[] k_input2 = {};
      sut.run(m_input2, true, n_input2, k_input2);
      final Item result2 = bom.getValue("run#6#8#0");
      result2.setNanoTime(23456);
      result2.setCalls(1);

      sut.runLinear(1000);
      final Item result3 = bom.getValue("runLinear#1000");
      result3.setNanoTime(2000);
      result3.setCalls(1);

      sut.runLinear(100);
      final Item result4 = bom.getValue("runLinear#100");
      result4.setNanoTime(1000);
      result4.setCalls(1);

      // ACT
      final Table<Integer, String, Double> resultTableRun = bom.getResultTable("run");
      final Table<Integer, String, Double> resultTableLinear = bom.getResultTable("runLinear");

      // ASSERT
      final StringBuilder expected = new StringBuilder();
      expected.append("N1\tN2\tN3\tTIME".concat(NL));
      expected.append("6\t8\t0\t23456".concat(NL));
      expected.append("8\t4\t5\t12345".concat(NL));
      final String actualRun = BigOReports.createDataReport(resultTableRun);
      Assert.assertEquals(expected.toString(), actualRun);

      final StringBuilder expected2 = new StringBuilder();
      expected2.append("N1\tTIME".concat(NL));
      expected2.append("100\t1000".concat(NL));
      expected2.append("1000\t2000".concat(NL));
      final String actualRunLinear = BigOReports.createDataReport(resultTableLinear);
      Assert.assertEquals(expected2.toString(), actualRunLinear);
   }

   @Test
   public void getResultTable_FourMixedCalls_GetCorrectValues() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

      sut.runLinear(10);
      final Item result = bom.getValue("runLinear#10");
      result.setNanoTime(123);
      result.setCalls(1);

      sut.runLinear(100);
      final Item result2 = bom.getValue("runLinear#100");
      result2.setNanoTime(345);
      result2.setCalls(1);

      sut.runLinear(1000);
      final Item result3 = bom.getValue("runLinear#1000");
      result3.setNanoTime(2000);
      result3.setCalls(1);

      sut.runLinear(100);
      final Item result4 = bom.getValue("runLinear#100");
      result4.setNanoTime(1000);
      result4.setCalls(2);

      // ACT
      final Table<Integer, String, Double> resultTable = bom.getResultTable("runLinear");

      // ASSERT
      final String actual = BigOReports.createDataReport(resultTable);
      final StringBuilder expected = new StringBuilder();
      expected.append("N1\tTIME".concat(NL));
      expected.append("10\t123".concat(NL));
      expected.append("100\t500".concat(NL));
      expected.append("1000\t2000".concat(NL));
      Assert.assertEquals(expected.toString(), actual);
   }

   @Test
   public void setNanoTime_SetNewValue_OldSumIsOverwritten() {
      // ARRANG
      final Item bigOProbe = new Item();
      bigOProbe.addTime(123);
      bigOProbe.addTime(234);

      // ACT
      bigOProbe.setNanoTime(456);

      // ASSERT
      Assert.assertEquals(456, bigOProbe.getTime());
   }

   @Test
   public void addNanoTime_TwoValues_SumIsCorrect() {
      // ARRANG
      final Item bigOProbe = new Item();

      // ACT
      bigOProbe.addTime(123);
      bigOProbe.addTime(234);

      // ASSERT
      Assert.assertEquals(357, bigOProbe.getTime());
   }

   @Test
   public void addNanoTime_OneValue_SumIsCorrect() {
      // ARRANG
      final Item bigOProbe = new Item();

      // ACT
      bigOProbe.addTime(123);

      // ASSERT
      Assert.assertEquals(123, bigOProbe.getTime());
   }

   @Test
   public void addNanoTime_OneValue_CallsAsExpectedTwo() {
      // ARRANG
      final Item bigOProbe = new Item();

      // ACT
      bigOProbe.addTime(123);
      bigOProbe.addTime(234);

      // ASSERT
      Assert.assertEquals(2, bigOProbe.getCalls());
   }

   @Test
   public void addNanoTime_OneValue_CallsAsExpectedOne() {
      // ARRANG
      final Item bigOProbe = new Item();

      // ACT
      bigOProbe.addTime(123);

      // ASSERT
      Assert.assertEquals(1, bigOProbe.getCalls());
   }

   @Test
   public void getKeys_AllParameter_GetCorrectKeys() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

      final int[] in01 = { 11, 22 };
      final long[] in02 = { 11, 22, 33 };
      final float[] in03 = { 11.4f, 2.1f, 2.23f, 12.2f };
      final double[] in04 = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      final byte[] in05 = { 11, 22, 33, 44, 55, 66 };
      final String in06 = "1234567";
      final List<Integer> in07 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final Set<Integer> in08 = new TreeSet<Integer>();
      in08.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10, 11));
      final Map<Integer, Integer> in09 = new HashMap<Integer, Integer>();
      in09.put(1, 1);
      in09.put(2, 2);
      in09.put(3, 3);
      in09.put(4, 4);
      in09.put(5, 1);
      in09.put(6, 2);
      in09.put(7, 3);
      in09.put(8, 4);
      in09.put(9, 4);
      in09.put(10, 4);
      final int in10 = 11;
      final long in11 = 12;
      sut.runAllParameter(in01, in02, in03, in04, in05, in06, in07, in08, in09, in10, in11);

      // ACT
      final Set<String> actual = bom.getKeys();

      // ASSERT
      final Set<String> expectedKeys = new HashSet<String>();
      expectedKeys.add("runAllParameter#2#3#4#5#6#7#8#9#10#11#12");
      Assert.assertEquals(expectedKeys, actual);
   }

   @Test
   public void runNotSupportedParameter_wrongParameterType_GetIllegalStateException() {
      // ARRANGE
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

      // ACT
      String actual = "";
      final String expected = "Not supported data type 'class java.io.File' for BigOAnalysis in method runNotSupportedParameter";
      try {
         sut.runNotSupportedParameter(null);
      } catch (final IllegalStateException ex) {
         actual = ex.getMessage();
      }

      // ASSERT
      Assert.assertEquals(expected, actual);
   }

}
