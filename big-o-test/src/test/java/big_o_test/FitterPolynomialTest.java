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

import big_o_test.math.FitterPolynomial;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FitterPolynomialTest {

    @Test
    public void getCoefficient_PolynomialRegressionDataSecondDegree_CorrectCoefficient() {
        // given
        final Table<Integer, String, Double> input = createSevenPoints();
        final FitterPolynomial polynom = new FitterPolynomial();
        polynom.init(input.column("N1"), input.column("TIME"), 2);

        // when
        final double result = polynom.getCoefficient(1);

        // then
        assertEquals(2.0000000000002167, result, 1E-12);
    }

    @Test
    public void init_PolynomialRegressionDataSecondDegree_CorrectPolynom()  {
        // given
        final Table<Integer, String, Double> input = createSevenPoints();
        final FitterPolynomial polynom = new FitterPolynomial();

        // when
        polynom.init(input.column("N1"), input.column("TIME"), 2);

        // then
        final String expected = "Quadratic ".concat(String.format(Locale.US, "\t%.4f        \ty = ", 1.0)
                + "3.00E+00 * x^2 + 2.00E+00 * x^1 + 1.00E+00");
        assertEquals(expected, polynom.toString());
    }

    @Test
    public void init_PolynomialRegressionDataThirdDegree_CorrectPolynom()  {
        // given
        final Table<Integer, String, Double> input = createSevenPoints();
        final FitterPolynomial polynom = new FitterPolynomial();

        // when
        polynom.init(input.column("N1"), input.column("TIME"), 3);

        // then
        final String expected = "Polynomial".concat(String.format(Locale.US, "\t%.4f        \ty = ", 1.0)
                + "1.08E-14 * x^3 + 3.00E+00 * x^2 + 2.00E+00 * x^1 + 1.00E+00");
        assertEquals(expected, polynom.toString());
    }

    @Test
    public void getRSquareAdjusted_SevenDataPoints_GetCorrectCoefficiantOfDetermination()  {
        // given
        final Table<Integer, String, Double> input = createSevenPoints();
        final FitterPolynomial polynom = new FitterPolynomial();
        polynom.init(input.column("N1"), input.column("TIME"), 2);

        // when
        final double result = polynom.getRSquareAdjusted();

        // then
        assertEquals(1.0, result, 0.000000000000001);
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
    public void init_TwoDataPoints_Exception()  {
        // given
        final Table<Integer, String, Double> input = TreeBasedTable.create();
        input.put(1, "N1", 0.0);
        input.put(1, "TIME", 10.0);
        input.put(2, "N1", 10.0);
        input.put(2, "TIME", 12.0);
        final FitterPolynomial function = new FitterPolynomial();

        // when
        final String expected = "number of data points to do the fit is dependent from degree";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                function.init(input.column("N1"), input.column("TIME"), 2)
        );

        // then
        assertEquals(expected, exception.getMessage());
    }

}
