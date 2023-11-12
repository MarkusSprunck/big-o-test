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
import sw_engineering_candies.assertBigO.math.FitterPolynomial;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FitterPolynomialTest {

    @Test
    public void getCoefficient_getCorrectFit() {
        // given
        final Table<Integer, String, Double> input = createPoints();
        final FitterPolynomial fitter = new FitterPolynomial();

        // when
        fitter.init(input.column("N1"), input.column("TIME"), 2);

        // then - good correlation
        assertEquals(1.0, fitter.getRSquareAdjusted(), 0.00001);

        // then - expected function
        assertEquals(100.0, fitter.getCoefficient(0), 0.4);
        assertEquals(0.1, fitter.getCoefficient(1), 0.001);
        assertEquals(5.0, fitter.getCoefficient(2), 0.001);

        // then - correct calculation
        assertEquals(5243082.4, fitter.calculate(1024.0), 0.5);
        assertEquals(20971824.8, fitter.calculate(2048.0), 0.5);
        assertEquals(83886589.6, fitter.calculate(4096.0), 0.5);
        assertEquals(335545239.2, fitter.calculate(8192.0), 0.5);
        assertEquals(1342179018.4, fitter.calculate(16384.0), 0.5);
    }


    @Test
    public void init_PolynomialRegressionDataSecondDegree_CorrectPolynomial() {
        // given
        final Table<Integer, String, Double> input = createSevenPoints();
        final FitterPolynomial sut = new FitterPolynomial();

        // when
        sut.init(input.column("N1"), input.column("TIME"), 2);

        // then
        final String expected = "Quadratic \t1.0000        \ty = 3.00E+00 * x^2 + 2.00E+00 * x^1 + 1.00E+00";
        assertEquals(expected, sut.toString());
    }

    @Test
    public void init_PolynomialRegressionDataThirdDegree_CorrectPolynomial() {
        // given
        final Table<Integer, String, Double> input = createSevenPoints();
        final FitterPolynomial sut = new FitterPolynomial();

        // when
        sut.init(input.column("N1"), input.column("TIME"), 3);

        // then
        final String expected = "Cubic     \t1.0000        \ty = 1.08E-14 * x^3 + 3.00E+00 * x^2 + 2.00E+00 * x^1 + 1.00E+00";
        assertEquals(expected, sut.toString());
    }

    @Test
    public void init_PolynomialRegressionDataForthDegree_CorrectPolynomial() {
        // given
        final Table<Integer, String, Double> input = createSevenPoints2();
        final FitterPolynomial sut = new FitterPolynomial();

        // when
        sut.init(input.column("N1"), input.column("TIME"), 4);

        // then
        final String expected = "Polynomial\t1.0000        \ty = -6.93E-13 * x^4 + 3.00E+00 * x^3 + 2.00E+00 * x^2 + 1.00E+00 * x^1 + -6.52E-11";
        assertEquals(expected, sut.toString());
    }

    @Test
    public void getRSquareAdjusted_SevenDataPoints_GetCorrectCoefficientOfDetermination() {
        // given
        final Table<Integer, String, Double> input = createSevenPoints();
        final FitterPolynomial sut = new FitterPolynomial();
        sut.init(input.column("N1"), input.column("TIME"), 2);

        // when
        final double result = sut.getRSquareAdjusted();

        // then
        assertEquals(1.0, result, 0.000000000000001);
    }

    private Table<Integer, String, Double> createPoints() {
        final Table<Integer, String, Double> input;
        input = TreeBasedTable.create();
        int index = 1;
        for (double i = 1024; i <= 1024 * 1024 * 16; i *= 2) {
            input.put(index, "N1", i);
            input.put(index, "TIME", 5 * i * i + 0.1 * i + 100);
            index++;
        }
        return input;
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

    private Table<Integer, String, Double> createSevenPoints2() {
        final Table<Integer, String, Double> input;
        input = TreeBasedTable.create();
        input.put(2, "N1", 2.0);
        input.put(2, "TIME", 17.0 * 2);
        input.put(3, "N1", 3.0);
        input.put(3, "TIME", 34.0 * 3);
        input.put(4, "N1", 4.0);
        input.put(4, "TIME", 57.0 * 4);
        input.put(5, "N1", 5.0);
        input.put(5, "TIME", 86.0 * 5);
        input.put(6, "N1", 6.0);
        input.put(6, "TIME", 121.0 * 6);
        input.put(7, "N1", 7.0);
        input.put(7, "TIME", 162.0 * 7);
        input.put(1, "N1", 1.0);
        input.put(1, "TIME", 6.0);
        return input;
    }


    @Test
    public void init_TwoDataPoints_Exception() {
        // given
        final Table<Integer, String, Double> input = TreeBasedTable.create();
        input.put(1, "N1", 0.0);
        input.put(1, "TIME", 10.0);
        input.put(2, "N1", 10.0);
        input.put(2, "TIME", 12.0);
        final FitterPolynomial fitter = new FitterPolynomial();

        // when
        final String expected = "number of data points to do the fit is dependent from degree";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                fitter.init(input.column("N1"), input.column("TIME"), 2)
        );

        // then
        assertEquals(expected, exception.getMessage());
    }

}
