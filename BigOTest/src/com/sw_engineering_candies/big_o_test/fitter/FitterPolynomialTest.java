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

package com.sw_engineering_candies.big_o_test.fitter;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class FitterPolynomialTest {

   @Test
   public void getCoefficient_PolynomialRegressionDataSecondDegree_CorrectCoefficient() {
      // ARRANGE
      final Table<Integer, String, Double> input = createSevenPoints();
      final FitterPolynomial polynom = new FitterPolynomial();
      polynom.init(input.column("N1"), input.column("TIME"), 2);

      // ACT
      final double result = polynom.getCoefficient(1);

      // ASSERT
      Assert.assertEquals(2.0000000000002167, result, 1E-12);
   }

   @Test
   public void init_PolynomialRegressionDataSecondDegree_CorrectPolynom() {
      // ARRANGE
      final Table<Integer, String, Double> input = createSevenPoints();
      final FitterPolynomial polynom = new FitterPolynomial();

      // ACT
      polynom.init(input.column("N1"), input.column("TIME"), 2);

      // ASSERT
      final String expected = "Quadratic ".concat(String.format("\t%.4f        \ty = ", 1.0)
            + "3.00E+00 * x^2 + 2.00E+00 * x^1 + 1.00E+00");
      Assert.assertEquals(expected, polynom.toString());
   }

   @Test
   public void getRSquareAdjusted_SevenDataPoints_GetCorrectCoefficiantOfDetermination() {
      // ARRANGE
      final Table<Integer, String, Double> input = createSevenPoints();
      final FitterPolynomial polynom = new FitterPolynomial();
      polynom.init(input.column("N1"), input.column("TIME"), 2);

      // ACT
      final double result = polynom.getRSquareAdjusted();

      // ASSERT
      Assert.assertEquals(1.0, result, 0.000000000000001);
   }

   private Table<Integer, String, Double> createSevenPoints() {
      final Table<Integer, String, Double> input;
      input = TreeBasedTable.create();
      input.put(2, "N1", 2.0);
      input.put(2, "TIME", 17.0);
      input.put(3, "N1", 3.0);
      input.put(3, "TIME", 34.0);
      input.put(4, "N1", 4.0);
      input.put(4, "TIME", 57.0);
      input.put(5, "N1", 5.0);
      input.put(5, "TIME", 86.0);
      input.put(6, "N1", 6.0);
      input.put(6, "TIME", 121.0);
      input.put(7, "N1", 7.0);
      input.put(7, "TIME", 162.0);
      input.put(1, "N1", 1.0);
      input.put(1, "TIME", 6.0);
      return input;
   }

   @Test
   public void init_OneDataPoints_Exception() {
      // ARRANGE
      final Table<Integer, String, Double> input = TreeBasedTable.create();
      input.put(1, "N1", 0.0);
      input.put(1, "TIME", 10.0);
      final FitterPolynomial function = new FitterPolynomial();

      // ACT
      String actual = "";
      final String expected = "number of data points to do the fit is dependent from degree";
      try {
         function.init(input.column("N1"), input.column("TIME"), 2);
      } catch (final IllegalArgumentException ex) {
         actual = ex.getMessage();
      }

      // ASSERT
      Assert.assertEquals(expected, actual);
   }

}
