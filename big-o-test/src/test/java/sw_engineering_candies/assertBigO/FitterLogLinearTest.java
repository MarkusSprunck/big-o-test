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
import sw_engineering_candies.assertBigO.math.FitterLogLinear;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FitterLogLinearTest {

    @Test
    public void getRSquareAdjusted_GetCorrectFit() {
        // given
        final Table<Integer, String, Double> input = createPoints();
        final FitterLogLinear fitter = new FitterLogLinear();

        // when
        fitter.init(input.column("N1"), input.column("TIME"));

        // then - good correlation
        assertEquals(1.0, fitter.getRSquareAdjusted(), 0.000000000000001);

        // then - expected function
        assertEquals(5.0, fitter.getCoefficient(0), 0.001);
        assertEquals(3.0, fitter.getCoefficient(1), 0.001);

        // then - correct calculation
        assertEquals(5.49306144, fitter.calculate(1.00), 0.00000001);
        assertEquals(17.91759469, fitter.calculate(2.00), 0.00000001);
        assertEquals(49.69813299, fitter.calculate(4.00), 0.00000001);
        assertEquals(730.29571063, fitter.calculate(32.0), 0.00000001);
        assertEquals(1682.39851904, fitter.calculate(64.0), 0.00000001);
    }

    @Test
    public void init_PowerLawWithoutNoise_CorrectFunction() {
        // given
        final Table<Integer, String, Double> input = createPoints();
        final FitterLogLinear fitter = new FitterLogLinear();

        // when
        fitter.init(input.column("N1"), input.column("TIME"));

        // then
        String expected = String.format(Locale.US, "LogLinear\t%.4f  \ty = ", 1.0) +
                String.format(Locale.US, "%.2E", 5.0) + " * x * log( " +
                String.format(Locale.US, "%.2E", 3.0) + " * x )";
        assertEquals(expected, fitter.toString());
    }

    private Table<Integer, String, Double> createPoints() {
        final Table<Integer, String, Double> input;
        input = TreeBasedTable.create();
        int index = 1;
        for (int i = 1; i <= 100; i *= 2) {
            input.put(index, "N1", (double) i);
            input.put(index, "TIME", (5 * (double) i * Math.log(3.0 * (double) i)));
            index++;
        }
        return input;
    }

    @Test
    public void init_OneDataPoints_Exception() {
        // given
        final Table<Integer, String, Double> input = TreeBasedTable.create();
        input.put(1, "N1", 0.0);
        input.put(1, "TIME", 10.0);
        final FitterLogLinear function = new FitterLogLinear();

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                function.init(input.column("N1"), input.column("TIME"))
        );

        // then
        assertEquals("need minimum 2 data points to do the fit", exception.getMessage());

    }

}
