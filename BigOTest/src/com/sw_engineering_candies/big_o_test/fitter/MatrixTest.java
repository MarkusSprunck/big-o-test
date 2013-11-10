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

/**
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote
 *   products derived from this software without specific prior
 *   written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
import org.junit.Assert;
import org.junit.Test;

public class MatrixTest {

   @Test
   public void solve_PolynomialRegressionThirdDegree_CorrectCorrelationCoefficients() {

      // ARRANGE
      final double[][] points = new double[][] { { -1, -1 }, { 0, 3 }, { 1, 2.5 }, { 2, 5 }, { 3, 4 }, { 5, 2 },
            { 7, 5 }, { 9, 4 } };
      // Polynomial degree 3, 8 x,y data pairs.
      // Correlation coefficient (r^2) = 0.6614129183293465
      // Standard error = 1.2393490986012985
      // Coefficient output form: simple list:
      //
      // 1.8942743159231679e+000
      // 1.8132651038133907e+000
      // -4.4164664000338555e-001
      // 3.0260423355553609e-002
      //
      // see http://www.arachnoid.com/sage/polynomial.html
      final int numberOfPoints = points.length;
      final int degree = 3;
      final int equations = degree + 1;
      final Matrix A = new Matrix(equations, equations);
      final Matrix b = new Matrix(equations, 1);
      for (int pointIndex = 0; pointIndex < numberOfPoints; pointIndex++) {
         final double x = points[pointIndex][0];
         final double y = points[pointIndex][1];
         for (int row = 0; row < equations; row++) {
            for (int col = 0; col < equations; col++) {
               A.setValue(row, col, A.getValue(row, col) + Math.pow(x, row + col));
            }
            b.setValue(row, 0, b.getValue(row, 0) + Math.pow(x, row) * y);
         }
      }

      // ACT
      final Matrix result = A.solve(b);

      // ASSERT
      Assert.assertEquals(1.8942743159231679, result.getValue(0, 0), 1.0E-10);
      Assert.assertEquals(1.8132651038133907, result.getValue(1, 0), 1.0E-10);
      Assert.assertEquals(-0.44164664000338555, result.getValue(2, 0), 1.0E-10);
   }

   @Test
   public void solve_PolynomialRegressionDataSecondDegree_CorrectCorrelationCoefficients() {

      // ARRANGE
      final double[][] points = new double[][] { { -1, -1 }, { 0, 3 }, { 1, 2.5 }, { 2, 5 }, { 3, 4 }, { 5, 2 },
            { 7, 5 }, { 9, 4 } };
      // Polynomial degree 2, 8 x,y data pairs.
      // Correlation coefficient (r^2) = 0.49210052111119
      // Standard error = 1.5179146298460922
      // Coefficient output form:
      //
      // 1.6484391286220554e+000
      // 9.9555711903272903e-001
      // -8.5717636022514102e-002
      //
      // see http://www.arachnoid.com/sage/polynomial.html
      final int numberOfPoints = points.length;
      final int degree = 2;
      final int equations = degree + 1;
      final Matrix A = new Matrix(equations, equations);
      final Matrix b = new Matrix(equations, 1);
      for (int pointIndex = 0; pointIndex < numberOfPoints; pointIndex++) {
         final double x = points[pointIndex][0];
         final double y = points[pointIndex][1];
         for (int row = 0; row < equations; row++) {
            for (int col = 0; col < equations; col++) {
               A.setValue(row, col, A.getValue(row, col) + Math.pow(x, row + col));
            }
            b.setValue(row, 0, b.getValue(row, 0) + Math.pow(x, row) * y);
         }
      }

      // ACT
      final Matrix result = A.solve(b);

      // ASSERT
      Assert.assertEquals(1.6484391286220554, result.getValue(0, 0), 1.0E-10);
      Assert.assertEquals(0.99555711903272903, result.getValue(1, 0), 1.0E-10);
      Assert.assertEquals(-0.085717636022514102, result.getValue(2, 0), 1.0E-10);

   }

   @Test
   public void solve_MinimalMatrix_CorrectResult() {
      // ARRANGE
      final Matrix A = new Matrix(new double[][] { { 4, 1 }, { 1, 3 } });
      final Matrix b = new Matrix(new double[][] { { 1, 2 } }).transpose();

      // ACT
      final Matrix result = A.solve(b);

      // ASSERT
      Assert.assertEquals(0.09090909090909091, result.getValue(0, 0), 1.0E-10);
      Assert.assertEquals(0.6363636363636364, result.getValue(1, 0), 1.0E-10);

   }

}