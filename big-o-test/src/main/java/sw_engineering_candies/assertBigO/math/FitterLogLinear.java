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
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.DiagonalMatrix;

import java.util.*;

/**
 * This class fits log-linear function: Y = a0 *x * log ( a1 * x )
 */
public class FitterLogLinear extends FitterBase {

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
        return coefficients.get(0) * x * Math.log(coefficients.get(1) * x);
    }

    private void calculateCoefficients() {
        final AbstractCurveFitter fitter = new AbstractCurveFitter() {
            @Override
            protected LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> points) {

                final double[] target  = new double[points.size()];
                final double[] weights = new double[points.size()];
                final double[] initialGuess = new double[]{2.0, 0.5};

                int i = 0;
                for(WeightedObservedPoint point : points) {
                    target[i]  = point.getY();
                    weights[i] = point.getWeight();
                    i += 1;
                }

                final AbstractCurveFitter.TheoreticalValuesFunction model =
                        new AbstractCurveFitter.TheoreticalValuesFunction(new LogLinearFunc(), points);

                return new LeastSquaresBuilder().
                        maxEvaluations(Integer.MAX_VALUE).
                        maxIterations(Integer.MAX_VALUE).
                        start(initialGuess).
                        target(target).
                        weight(new DiagonalMatrix(weights)).
                        model(model.getModelFunction(), model.getModelFunctionJacobian()).
                        build();

            }
        };

        // Collect
        Collection<WeightedObservedPoint> collection = new ArrayList<>();
        for (int pointIndex = 1; pointIndex <= super.xValues.size(); pointIndex++) {
            final double x = super.xValues.get(pointIndex);
            final double y = super.yValues.get(pointIndex);
            collection.add( new WeightedObservedPoint(1, x, y));
        }

        final double[] estimatedCoefficients = fitter.fit(collection);

        super.coefficients.add(0, estimatedCoefficients[0]);
        super.coefficients.add(1, estimatedCoefficients[1]);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "LogLinear\t%.4f  \ty = ", getRSquareAdjusted()) +
                String.format(Locale.US, "%.2E", coefficients.get(0)) + " * x * log( " +
                String.format(Locale.US, "%.2E", coefficients.get(1)) + " * x )";
    }


    private static class LogLinearFunc implements ParametricUnivariateFunction {

        public double value(double x, double... parameters) {
            final double a = parameters[0];
            final double b = parameters[1];
            return a * x * Math.log(b * x);
        }

        public double[] gradient(double x, double... parameters) {

            final double a = parameters[0];
            final double b = parameters[1];
            final double[] gradients = new double[2];

            // derivative with respect to a
            gradients[0] = x * Math.log(b * x);

            // derivative with respect to b
            gradients[1] = a * x / b;

            return gradients;

        }
    }

}
