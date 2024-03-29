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

package sw_engineering_candies.assertBigO.math;

import com.google.common.base.Preconditions;

import java.util.Locale;
import java.util.Map;

/**
 * Fit power-law function: Y = a0 * x ^ a1
 * <p>
 * See <a href="http://mathworld.wolfram.com/LeastSquaresFittingPowerLaw.html">...</a>
 */
public class FitterPowerLaw extends FitterBase {

    /**
     * Set all the input data and execute fit
     */
    public void init(Map<Integer, Double> xValues, Map<Integer, Double> yValues) {
        // check preconditions
        Preconditions.checkNotNull(yValues);
        Preconditions.checkArgument(xValues.size() >= 2, "need minimum 2 data points to do the fit");

        super.xValues = xValues;
        super.yValues = yValues;
        super.numberOfParameters = 2;

        calculateCoefficients();
        calculateCoefficientOfDeterminationLogarithmicData();
    }

    /**
     * Calculates the fitted function for point x
     */
    @Override
    public double getY(final double x) {
        return coefficients.get(0) * Math.pow(x, coefficients.get(1));
    }

    private void calculateCoefficients() {
        double sum1 = 0.0;
        double sum2 = 0.0;
        double sum3 = 0.0;
        double sum4 = 0.0;
        final int n = super.xValues.size();
        for (int pointIndex = 1; pointIndex <= n; pointIndex++) {
            final double x = super.xValues.get(pointIndex);
            final double y = super.yValues.get(pointIndex);
            sum1 += Math.log(y) * Math.log(x);
            sum2 += Math.log(x);
            sum3 += Math.log(y);
            sum4 += Math.log(x) * Math.log(x);
        }
        final double b = (n * sum1 - sum2 * sum3) / (n * sum4 - sum2 * sum2);
        final double a = Math.exp((sum3 - b * sum2) / n);
        super.coefficients.add(0, a);
        super.coefficients.add(1, b);
    }

    @Override
    public String toString() {
        String a0 = String.format(Locale.US, "%.2E", coefficients.get(0));
        String a1 = String.format(Locale.US, "%.2E", coefficients.get(1));
        String prefix = String.format(Locale.US, "PowerLaw\t%.4f  \t", getRSquareAdjusted());
        return prefix + "y = " + a0 + " * x^" + a1;
    }

    @Override
    public double calculate(double x) {
        return coefficients.get(0) * Math.pow(x, coefficients.get(1));
    }

}
