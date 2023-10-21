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

package big_o_test;

import big_o_test.math.FitterPowerLaw;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class FitterPowerLawTest {

   @Test
   public void getRSquareAdjusted_HunderedDataPoints_GetCorrectCoefficiantOfDetermination() {
      // ARRANGE
      final Table<Integer, String, Double> input = createTenPoints();
      final FitterPowerLaw fitter = new FitterPowerLaw();
      fitter.init(input.column("N1"), input.column("TIME"));

      // ACT
      final double result = fitter.getRSquareAdjusted();

      // ASSERT
      Assert.assertEquals(1.0, result, 0.000000000000001);
   }

   @Test
   public void init_PowerLawWithoutNoise_CorrectFunction() {
      // ARRANGE
      final Table<Integer, String, Double> input = createTenPoints();
      final FitterPowerLaw fitter = new FitterPowerLaw();

      // ACT
      fitter.init(input.column("N1"), input.column("TIME"));

      // ASSERT
      final StringBuilder expected = new StringBuilder(100);
      expected.append(String.format(Locale.US,"PowerLaw\t%.4f  \ty = ", 1.0));
      expected.append(String.format(Locale.US,"%.2E", 10.0));
      expected.append(" * x^");
      expected.append(String.format(Locale.US,"%.2E", 1.1));
      Assert.assertEquals(expected.toString(), fitter.toString());
   }

   private Table<Integer, String, Double> createTenPoints() {
      final Table<Integer, String, Double> input;
      input = TreeBasedTable.create();
      for (int i = 1; i <= 100; i++) {
         input.put(i, "N1", (double) i);
         input.put(i, "TIME", (10.0 * Math.pow(i, 1.1)));
      }
      return input;
   }

   @Test
   public void init_OneDataPoints_Exception() {
      // ARRANGE
      final Table<Integer, String, Double> input = TreeBasedTable.create();
      input.put(1, "N1", 0.0);
      input.put(1, "TIME", 10.0);
      final FitterPowerLaw function = new FitterPowerLaw();

      // ACT
      String actual = "";
      final String expected = "need minimum 2 data points to do the fit";
      try {
         function.init(input.column("N1"), input.column("TIME"));
      } catch (final IllegalArgumentException ex) {
         actual = ex.getMessage();
      }

      // ASSERT
      Assert.assertEquals(expected, actual);
   }

}
