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

import java.util.Map;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.CurveFitter;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;

import com.google.common.base.Preconditions;

public class FitterLogLinear extends FitterAbstractBase {

   /**
    * Fit linear log function: Y = a0 *x * log ( a1 * x )
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
    * Calculates the fitted polynomial for point x
    */
   @Override
   public double getY(final double x) {
      return coefficients.get(0) * x * Math.log(coefficients.get(1) * x);
   }

   private void calculateCoefficients() {
      final LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
      final CurveFitter<ParametricUnivariateFunction> curveFitter = new CurveFitter<ParametricUnivariateFunction>(
            optimizer);

      for (int pointIndex = 1; pointIndex <= super.xValues.size(); pointIndex++) {
         final double x = super.xValues.get(pointIndex);
         final double y = super.yValues.get(pointIndex);
         curveFitter.addObservedPoint(x, y);
      }

      final ParametricUnivariateFunction f = new ParametricUnivariateFunction() {

         @Override
         public double value(double x, double... parameters) {
            final double a = parameters[0];
            final double b = parameters[1];
            return a * x * Math.log(b * x);
         }

         @Override
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
      };

      final double[] initialGuess = new double[] { 2.0, 0.5 };
      final double[] estimatedParameters = curveFitter.fit(f, initialGuess);

      super.coefficients.add(0, estimatedParameters[0]);
      super.coefficients.add(1, estimatedParameters[1]);
   }

   @Override
   public String toString() {
      final StringBuilder result = new StringBuilder(100);
      result.append(String.format("LogLinear\t%.4f  \ty = ", getRSquareAdjusted()));
      result.append(String.format("%.2E", coefficients.get(0)));
      result.append(" * x * log( ");
      result.append(String.format("%.2E", coefficients.get(1)));
      result.append(" * x )");
      return result.toString();
   }

}
