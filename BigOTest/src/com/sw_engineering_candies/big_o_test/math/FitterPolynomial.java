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

package com.sw_engineering_candies.big_o_test.math;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealVector;

import com.google.common.base.Preconditions;

/**
 * This class fits Polynomial function: Y = a0 + a1 * x + ... + aN * x^N. A degree polynomial has
 * (degree+1) coefficients.
 */
public class FitterPolynomial extends FitterBase {

   /**
    * Set all the input data and execute fit
    */
   public void init(Map<Integer, Double> xValues, Map<Integer, Double> yValues, int degree) {
      // check preconditions
      Preconditions.checkNotNull(yValues);
      Preconditions.checkArgument(xValues.size() >= degree + 1,
            "number of data points to do the fit is dependent from degree");

      super.xValues = xValues;
      super.yValues = yValues;
      super.numberOfParameters = degree + 1;

      calculateCoefficients(degree);
      calculateCoefficientOfDeterminationOriginalData();
   }

   /**
    * Calculates the fitted function for point x
    */
   @Override
   public double getY(final double x) {
      double result = 0.0;
      for (int p = 0; p < coefficients.size(); p++) {
         result += coefficients.get(p) * Math.pow(x, p);
      }
      return result;
   }

   /**
    * Calculates the Polynomial Regression
    * 
    * see http://www.arachnoid.com/sage/polynomial.html
    * 
    * @param degree
    *           Is the degree of the polynomial
    */
   private void calculateCoefficients(int degree) {
      final int numberOfPoints = super.xValues.size();
      final int equations = degree + 1;
      final Array2DRowRealMatrix A = new Array2DRowRealMatrix(equations, equations);
      final ArrayRealVector b = new ArrayRealVector(equations);
      for (int pointIndex = 1; pointIndex <= numberOfPoints; pointIndex++) {
         final double x = super.xValues.get(pointIndex);
         final double y = super.yValues.get(pointIndex);
         for (int row = 0; row < equations; row++) {
            for (int col = 0; col < equations; col++) {
               A.addToEntry(row, col, Math.pow(x, row + col));
            }
            b.addToEntry(row, Math.pow(x, row) * y);
         }
      }
      final DecompositionSolver solver = new LUDecomposition(A).getSolver();
      final RealVector solution = solver.solve(b);

      coefficients.clear();
      for (int i = 0; i <= degree; i++) {
         coefficients.add(i, solution.getEntry(i));
      }
   }

   /**
    * Creates the equation of the fitted polynomial
    */
   @Override
   public String toString() {
      final StringBuilder equation = new StringBuilder(100);
      final int maxIndex = coefficients.size() - 1;
      for (int index = maxIndex; index >= 0; index--) {
         equation.append(String.format(Locale.US,"%.2E", coefficients.get(index)));
         if (index > 0) {
            equation.append(" * x^").append(index).append(" + ");
         }
      }
      final StringBuilder result = new StringBuilder();
      result.append(getPolynomialType());
      result.append(String.format(Locale.US, "\t%.4f        \ty = ", getRSquareAdjusted()));
      result.append(equation.toString());
      return result.toString();
   }

   private String getPolynomialType() {
      String type;
      switch (coefficients.size()) {
      case 1: {
         type = "Constant  ";
         break;
      }
      case 2: {
         type = "Linear    ";
         break;
      }
      case 3: {
         type = "Quadratic ";
         break;
      }
      default: {
         type = "Polynomial";
      }
      }
      return type;
   }

}