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

package com.sw_engineering_candies.big_o_test.tests;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.sw_engineering_candies.big_o_test.math.FitterExponential;

public class FitterExponentialTest {

   @Test
   public void getRSquareAdjusted_TenDataPoints_GetCorrectCoefficiantOfDetermination() {
      // ARRANGE
      final Table<Integer, String, Double> input = createTenPoints();
      final FitterExponential polynom = new FitterExponential();
      polynom.init(input.column("N1"), input.column("TIME"));

      // ACT
      final double result = polynom.getRSquareAdjusted();

      // ASSERT
      Assert.assertEquals(1.0, result, 0.000000000000001);
   }

   @Test
   public void init_ExponentalFunctionWithoutNoise_CorrectFunction() {
      // ARRANGE
      final Table<Integer, String, Double> input = createTenPoints();
      final FitterExponential exponentialFunction = new FitterExponential();

      // ACT
      exponentialFunction.init(input.column("N1"), input.column("TIME"));

      // ASSERT
      final StringBuilder expected = new StringBuilder(100);
      expected.append(String.format(Locale.US,"Exponential\t%.4f  \ty = ", 1.0));
      expected.append(String.format(Locale.US,"%.2E", 100.0));
      expected.append(" * exp ( ");
      expected.append(String.format(Locale.US,"%.2E", 0.5));
      expected.append(" * x )");
      Assert.assertEquals(expected.toString(), exponentialFunction.toString());
   }

   @Test
   public void init_OneDataPoints_Exception() {
      // ARRANGE
      final Table<Integer, String, Double> input = TreeBasedTable.create();
      input.put(1, "N1", 0.0);
      input.put(1, "TIME", 10.0);
      final FitterExponential exponentialFunction = new FitterExponential();

      // ACT
      String actual = "";
      final String expected = "need minimum 2 data points to do the fit";
      try {
         exponentialFunction.init(input.column("N1"), input.column("TIME"));
      } catch (final IllegalArgumentException ex) {
         actual = ex.getMessage();
      }

      // ASSERT
      Assert.assertEquals(expected, actual);
   }

   private Table<Integer, String, Double> createTenPoints() {
      final Table<Integer, String, Double> input;
      input = TreeBasedTable.create();
      for (int i = 1; i <= 10; i++) {
         input.put(i, "N1", (double) i);
         input.put(i, "TIME", (100.0 * Math.exp(0.5 * i)));
      }
      return input;
   }

}
