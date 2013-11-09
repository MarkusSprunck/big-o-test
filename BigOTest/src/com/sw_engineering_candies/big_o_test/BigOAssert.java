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

package com.sw_engineering_candies.big_o_test;

import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.fitter.FitterPolynomial;

public class BigOAssert {

   private static final double DEGREE_ALLOWED_DELTA = 0.2;

   private static final String NL = System.getProperty("line.separator");

   public static void assertConstant(BigOAnalyser bom, String methodName) {
      // fetch measured data
      final Table<Integer, String, Double> resultTable = getDataTableChecked(bom, methodName);
      // constant functions should have a polynomial degree of 0.0
      assertPolynomialDegree(bom, methodName, 0.0, DEGREE_ALLOWED_DELTA);
      // find the best fit function
      final String details = BigOReports.calculateBestFunction(resultTable);
      if (!details.startsWith("Polynomial")) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException assertConstant failed:").append(NL).append(details);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertLinear(BigOAnalyser bom, String methodName) {
      // fetch measured data
      final Table<Integer, String, Double> resultTable = getDataTableChecked(bom, methodName);
      // linear functions should have a polynomial degree of 1.0
      assertPolynomialDegree(bom, methodName, 1.0, DEGREE_ALLOWED_DELTA);
      // find the best fit function
      final String details = BigOReports.calculateBestFunction(resultTable);
      if (!(details.startsWith("Polynomial") || details.startsWith("PowerLaw"))) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException assertLinear failed:").append(NL).append(details);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertLogLinearOrPowerLaw(BigOAnalyser bom, String methodName) {
      // fetch measured data
      final Table<Integer, String, Double> resultTable = getDataTableChecked(bom, methodName);
      // log-linear functions should have a polynomial degree of 1.1
      assertPolynomialDegree(bom, methodName, 1.1, DEGREE_ALLOWED_DELTA);
      // find the best fit function
      final String details = BigOReports.calculateBestFunction(resultTable);
      if (!(details.startsWith("LogLinear") || details.startsWith("PowerLaw"))) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException assertLogLinear failed:").append(NL).append(details);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertQuadratic(BigOAnalyser bom, String methodName) {
      // fetch measured data
      final Table<Integer, String, Double> resultTable = getDataTableChecked(bom, methodName);
      // quadratic functions should have a polynomial degree of 2.0
      assertPolynomialDegree(bom, methodName, 2.0, DEGREE_ALLOWED_DELTA);
      // find the best fit function
      final String details = BigOReports.calculateBestFunction(resultTable);
      if (!details.startsWith("Polynomial")) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException assertQuadratic failed:").append(NL).append(details);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertPowerLaw(BigOAnalyser bom, String methodName) {
      // fetch measured data
      final Table<Integer, String, Double> resultTable = getDataTableChecked(bom, methodName);
      // find the best fit function
      final String details = BigOReports.calculateBestFunction(resultTable);
      if (!details.startsWith("PowerLaw")) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException assertPowerLaw failed:").append(NL).append(details);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static Double estimatePolynomialDegree(Table<Integer, String, Double> resultTable) {
      // calculate logarithms of both axis
      final Map<Integer, Double> xValues = new TreeMap<Integer, Double>();
      final Map<Integer, Double> yValues = new TreeMap<Integer, Double>();
      for (int index = 1; index <= resultTable.column("N1").size(); index++) {
         xValues.put(index, Math.log10(resultTable.column("N1").get(index)));
         yValues.put(index, Math.log10(resultTable.column("TIME").get(index)));
      }
      // fit polynomial of first degree (a0 + a1 * x)
      final FitterPolynomial polynom = new FitterPolynomial();
      polynom.init(xValues, yValues, 1);
      // coefficient of the linear term a1 it what we need
      final double result = polynom.getCoefficient(1);
      // check the quality of the fit in cases the function is not constant
      if (result > 0.8) {
         final double coefficientOfDetermination = polynom.getRSquareAdjusted();
         Preconditions.checkState(coefficientOfDetermination > 0.8, "R^2=" + coefficientOfDetermination);
      }
      return result;
   }

   public static void assertPolynomialDegree(BigOAnalyser bom, String method, double expected, double range) {
      // fetch measured data
      final Table<Integer, String, Double> resultTable = getDataTableChecked(bom, method);
      // estimate polynomial degree
      final double estimate = estimatePolynomialDegree(resultTable);
      // assert that degree is in expected range
      if (estimate < expected - range || estimate > expected + range) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException assertPolynomialDegree failed:");
         message.append(NL).append("\tPolynomial degree expected = ").append(expected);
         message.append(NL).append("\tPolynomial degree actual   = ").append(estimate);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   private static Table<Integer, String, Double> getDataTableChecked(BigOAnalyser bom, String method) {
      // check preconditions
      Preconditions.checkNotNull(bom);
      Preconditions.checkNotNull(method);
      Preconditions.checkArgument(!method.isEmpty());
      // fetch data table
      final Table<Integer, String, Double> resultTable = bom.getResultTable(method);
      // check size of data point table
      final boolean isNumberOfDataPointsSufficient = resultTable.column("TIME").size() >= 4;
      final String message = "minimum 4 data points are needed for a reliable analysis";
      Preconditions.checkState(isNumberOfDataPointsSufficient, message);
      return resultTable;
   }

}
