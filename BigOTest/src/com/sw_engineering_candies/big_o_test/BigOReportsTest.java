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
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.fitter.Item;
import com.sw_engineering_candies.big_o_test.utils.Algorithms;

public class BigOReportsTest {

   private static final String NL = System.getProperty("line.separator");

   final BigOAnalyser boa = new BigOAnalyser();

   @Test
   public void createDataReport_MoreCallsOfOneFunction_GetCorrectReport() {
      // ARRANGE
      final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);

      final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input = { 11, 22, 33, 44 };
      final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input, true, n_input, k_input);
      final Item result = boa.getValue("run#8#4#5");
      result.setNanoTime(12345);
      result.setCalls(1);

      final List<Integer> m_input2 = Arrays.asList(1, 2, 3, 4, 5, 6);
      final int[] n_input2 = { 11, 22, 33, 44, 55, 66, 77, 88 };
      final float[] k_input2 = {};
      sut.run(m_input2, true, n_input2, k_input2);
      final Item result2 = boa.getValue("run#6#8#0");
      result2.setNanoTime(23456);
      result2.setCalls(1);
      final Table<Integer, String, Double> data = boa.getResultTable("run");

      // ACT
      final String actual = BigOReports.createDataReport(data);

      // ASSERT
      final StringBuilder expected = new StringBuilder(100);
      expected.append("N1\tN2\tN3\tTIME".concat(NL));
      expected.append("6\t8\t0\t23456".concat(NL));
      expected.append("8\t4\t5\t12345".concat(NL));
      Assert.assertEquals(expected.toString(), actual);
   }

   @Test
   public void createDataReport_OneCallOfRunLinear_GetReport() {
      // ARRANGE
      final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
      sut.runLinear(10);
      final Item result = boa.getValue("runLinear#10");
      result.setNanoTime(123);
      result.setCalls(1);
      final Table<Integer, String, Double> data = boa.getResultTable("runLinear");

      // ACT
      final String actual = BigOReports.createDataReport(data);

      // ASSERT
      Assert.assertEquals("N1\tTIME".concat(NL).concat("10\t123").concat(NL), actual);
   }

   @Test
   public void createDataReport_FourCallsOfrunLinear_GetReport() {
      // ARRANGE
      final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);

      sut.runLinear(10);
      final Item result = boa.getValue("runLinear#10");
      result.setNanoTime(123);
      result.setCalls(1);

      sut.runLinear(100);
      final Item result2 = boa.getValue("runLinear#100");
      result2.setNanoTime(345);
      result2.setCalls(1);

      sut.runLinear(100);
      final Item result4 = boa.getValue("runLinear#100");
      result4.setNanoTime(1000);
      result4.setCalls(2);

      sut.runLinear(1000);
      final Item result3 = boa.getValue("runLinear#1000");
      result3.setNanoTime(567);
      result3.setCalls(1);

      final Table<Integer, String, Double> data = boa.getResultTable("runLinear");

      // ACT
      final String actual = BigOReports.createDataReport(data);

      // ASSERT
      final StringBuilder expected = new StringBuilder(100);
      expected.append("N1\tTIME".concat(NL));
      expected.append("10\t123".concat(NL));
      expected.append("100\t500".concat(NL));
      expected.append("1000\t567".concat(NL));
      Assert.assertEquals(expected.toString(), actual);
   }

   @Test
   public void createDataReport_OneCall_GetCorrectReport() {
      // ARRANGE
      final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);

      final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
      final int[] n_input = { 11, 22, 33, 44 };
      final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
      sut.run(m_input, true, n_input, k_input);
      final Item result = boa.getValue("run#8#4#5");
      result.setNanoTime(12345);
      result.setCalls(1);
      final Table<Integer, String, Double> data = boa.getResultTable("run");

      // ACT
      final String actual = BigOReports.createDataReport(data);

      // ASSERT
      final String expected = "N1\tN2\tN3\tTIME".concat(NL).concat("8\t4\t5\t12345").concat(NL);
      Assert.assertEquals(expected, actual);
   }

}
