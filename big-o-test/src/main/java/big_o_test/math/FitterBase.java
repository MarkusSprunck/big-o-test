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

package big_o_test.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is the base for all fitter implementations. It provides common functionality for all
 * fitters.
 */
public abstract class FitterBase {

    /**
     * Coefficients of the fitted Polynomial starting with a0=coefficients[0], a1=coefficients[1],
     * ...
     */
    protected final List<Double> coefficients = new ArrayList<Double>();
    /**
     * Number of parameters of fit
     */
    protected int numberOfParameters = 1;
    /**
     * All x Values of underlying data points
     */
    protected Map<Integer, Double> xValues = null;
    /**
     * All y Values of underlying data points
     */
    protected Map<Integer, Double> yValues = null;
    /**
     * Coefficient of Determination for the fitted polynomial and the underlying data points
     */
    private double coefficientOfDetermination = 0.0;

    /**
     * Calculates the fitted polynomial for point x
     */
    public abstract double getY(final double x);

    /**
     * Get a single coefficient of the fitted polynomial
     */
    public double getCoefficient(int index) {
        return coefficients.get(index);
    }

    /**
     * Get coefficient of determination for the fitted polynomial and the underlying data points
     */
    public double getRSquareAdjusted() {
        return coefficientOfDetermination;
    }

    /**
     * Calculates the Adjusted Coefficient of Determination
     * <p>
     * see http://en.wikipedia.org/wiki/Coefficient_of_determination
     * <p>
     * "The adjusted R2 accounts for the number of parameters fit by the regression, and so can be
     * compared between models with different numbers of parameters."
     * http://www.graphpad.com/guides/prism/6/curve-fitting/index.htm?reg_diagnostics_tab_7_2.htm
     */
    protected void calculateCoefficientOfDeterminationLogarithmicData() {

        // Calculate the mean value of y values
        double ySum = 0.0;
        int number = 0;
        final int n = xValues.size();
        for (int index = 1; index <= n; index++) {
            final double fit = getY(xValues.get(index));
            if (fit > 1) {
                ySum += yValues.get(index);
                number++;
            }
        }
        final double yMean = ySum / number;

        // Coefficient of determination
        double SS_tot = 0.0;
        double SS_res = 0.0;
        for (int index = 1; index <= n; index++) {
            final double x = xValues.get(index);
            final double y = Math.log(yValues.get(index));
            final double fit = getY(x);
            if (fit > 1) {
                SS_tot += Math.pow(y - Math.log(yMean), 2);
                SS_res += Math.pow(y - Math.log(fit), 2);
            }
        }
        coefficientOfDetermination = 1.0 - (SS_res / (number - numberOfParameters)) / (SS_tot / (number - 1));
    }

    protected void calculateCoefficientOfDeterminationOriginalData() {

        // Calculate the mean value of y values
        double ySum = 0.0;
        int number = 0;
        final int n = xValues.size();
        for (int index = 1; index <= n; index++) {
            ySum += yValues.get(index);
            number++;
        }
        final double yMean = ySum / number;

        // Coefficient of determination
        double SS_tot = 0.0;
        double SS_res = 0.0;
        for (int index = 1; index <= n; index++) {
            final double x = xValues.get(index);
            final double y = yValues.get(index);
            final double fit = getY(x);
            SS_tot += Math.pow(y - yMean, 2);
            SS_res += Math.pow(y - fit, 2);
        }
        coefficientOfDetermination = 1.0 - (SS_res / (number - numberOfParameters)) / (SS_tot / (number - 1));
    }

}