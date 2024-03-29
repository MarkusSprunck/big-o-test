/*
 * Copyright (C) 2013-2023, Markus Sprunck <sprunck.markus@gmail.com>
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

package sw_engineering_candies.assertBigO;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.junit.jupiter.api.Test;
import sw_engineering_candies.assertBigO.math.FitterLogarithmic;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FitterLogarithmicTest {

    @Test
    public void getRSquareAdjusted_getCorrectFit() {
        // given
        final Table<Integer, String, Double> input = createPoints();
        final FitterLogarithmic fitter = new FitterLogarithmic();

        // when
        fitter.init(input.column("N1"), input.column("TIME"));

        // then - good correlation
        assertEquals(1.0, fitter.getRSquareAdjusted(), 0.000000000000001);

        // then - expected function
        assertEquals(100.0, fitter.getCoefficient(0), 0.001);
        assertEquals(10.5, fitter.getCoefficient(1), 0.001);

        // then - correct calculation
        assertEquals(100.00000000, fitter.calculate(1.000), 0.00000001);
        assertEquals(124.17714347, fitter.calculate(10.00), 0.00000001);
        assertEquals(131.45518887, fitter.calculate(20.00), 0.00000001);
        assertEquals(136.39022697, fitter.calculate(32.0), 0.00000001);
        assertEquals(148.35428695, fitter.calculate(100), 0.00000001);
    }

    @Test
    public void init_LogarithmicFunctionWithoutNoise_CorrectFunction() {
        // given
        final Table<Integer, String, Double> input = createPoints();
        final FitterLogarithmic fitter = new FitterLogarithmic();

        // when
        fitter.init(input.column("N1"), input.column("TIME"));

        // then
        String expected = String.format(Locale.US, "Logarithmic\t%.4f  \ty = ", 1.0) +
                String.format(Locale.US, "%.2E", 100.) +
                " + " +
                String.format(Locale.US, "%.2E", 10.5) +
                " * log ( x )";
        assertEquals(expected, fitter.toString());
    }

    @Test
    public void init_OneDataPoints_Exception() {
        // given
        final Table<Integer, String, Double> input = TreeBasedTable.create();
        input.put(1, "N1", 0.0);
        input.put(1, "TIME", 10.0);
        final FitterLogarithmic function = new FitterLogarithmic();

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                function.init(input.column("N1"), input.column("TIME"))
        );

        // then
        assertEquals("need minimum 2 data points to do the fit", exception.getMessage());
    }

    private Table<Integer, String, Double> createPoints() {
        final Table<Integer, String, Double> input;
        input = TreeBasedTable.create();
        for (int i = 1; i <= 100; i++) {
            input.put(i, "N1", (double) i);
            input.put(i, "TIME", (100.0 + 10.5 * Math.log(i)));
        }
        return input;
    }

}
