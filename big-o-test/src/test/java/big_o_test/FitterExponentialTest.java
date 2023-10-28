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

package big_o_test;

import big_o_test.math.FitterExponential;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FitterExponentialTest {

    @Test
    public void getRSquareAdjusted_TenDataPoints_GetCorrectCoefficiantOfDetermination()  {
        // given
        final Table<Integer, String, Double> input = createTenPoints();
        final FitterExponential polynom = new FitterExponential();
        polynom.init(input.column("N1"), input.column("TIME"));

        // when
        final double result = polynom.getRSquareAdjusted();

        // then
        assertEquals(1.0, result, 0.000000000000001);
    }

    @Test
    public void init_ExponentalFunctionWithoutNoise_CorrectFunction()  {
        // given
        final Table<Integer, String, Double> input = createTenPoints();
        final FitterExponential exponentialFunction = new FitterExponential();

        // when
        exponentialFunction.init(input.column("N1"), input.column("TIME"));

        // then
        String expected = String.format(Locale.US, "Exponential\t%.4f  \ty = ", 1.0) +
                String.format(Locale.US, "%.2E", 100.0) +
                " * exp ( " +
                String.format(Locale.US, "%.2E", 0.5) +
                " * x )";
        assertEquals(expected, exponentialFunction.toString());
    }

    @Test
    public void init_OneDataPoints_Exception()  {
        // given
        final Table<Integer, String, Double> input = TreeBasedTable.create();
        input.put(1, "N1", 0.0);
        input.put(1, "TIME", 10.0);
        final FitterExponential exponentialFunction = new FitterExponential();

        // when
        final String expected = "need minimum 2 data points to do the fit";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                exponentialFunction.init(input.column("N1"), input.column("TIME"))
        );

        // then
        assertEquals(expected, exception.getMessage());
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
