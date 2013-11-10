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

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;

/**
 * This class provides assert methods to check the empirically estimated Big-O time efficiency.
 */
public class BigOAssert {

   /**
    * Expected polynomial degrees
    */
   private static final double DEGREE_EXPECTED_QUADRATIC = 2.0;
   private static final double DEGREE_EXPECTED_LOGLINEAR = 1.1;
   private static final double DEGREE_EXPECTED_LINEAR = 1.0;
   private static final double DEGREE_EXPECTED_CONSTANT = 0.0;
   private static final double DEGREE_EXPECTED_DELTA = 0.2;

   /**
    * Use the platform independent line separator
    */
   private static final String NL = System.getProperty("line.separator");

   public static void assertPolynomialDegree(BigOAnalyser boa, String method, double expected, double delta) {
      // check preconditions
      Preconditions.checkNotNull(boa);
      Preconditions.checkNotNull(method);
      Preconditions.checkArgument(boa.isAnalysed(method), "here we need a analysed method name");
      Preconditions.checkArgument(0.0 <= expected);
      Preconditions.checkArgument(0.0 <= delta);

      // fetch measured data
      final Table<Integer, String, Double> data = boa.getResultTableChecked(method);

      // estimate polynomial degree
      final double estimate = BigOAnalyser.estimatePolynomialDegree(data);

      // assert that degree is in expected range
      if (estimate < expected - delta || estimate > expected + delta) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException - assertPolynomialDegree failed:");
         message.append(NL).append("\tPolynomial degree expected = ").append(expected);
         message.append(NL).append("\tPolynomial degree actual   = ").append(estimate);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertConstant(BigOAnalyser boa, String method) {
      // check preconditions
      Preconditions.checkNotNull(boa);
      Preconditions.checkNotNull(method);
      Preconditions.checkArgument(boa.isAnalysed(method), "here we need a analysed method name");
      
      // fetch measured data
      final Table<Integer, String, Double> data = boa.getResultTableChecked(method);

      // constant functions should have a polynomial degree of 0.0
      assertPolynomialDegree(boa, method, DEGREE_EXPECTED_CONSTANT, DEGREE_EXPECTED_DELTA);

      // find the best fit function and check type
      final String function = BigOReports.calculateBestFunction(data);
      if (!function.startsWith("Constant")) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException - assertConstant failed:");
         message.append(NL);
         message.append(function);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertLinear(BigOAnalyser boa, String method) {
      // check preconditions
      Preconditions.checkNotNull(boa);
      Preconditions.checkNotNull(method);
      Preconditions.checkArgument(boa.isAnalysed(method), "here we need a analysed method name");
      
      // fetch measured data
      final Table<Integer, String, Double> data = boa.getResultTableChecked(method);

      // linear functions should have a polynomial degree of 1.0
      assertPolynomialDegree(boa, method, DEGREE_EXPECTED_LINEAR, DEGREE_EXPECTED_DELTA);

      // find the best fit function and check type
      final String function = BigOReports.calculateBestFunction(data);
      if (!(function.startsWith("Linear") || function.startsWith("PowerLaw"))) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException - assertLinear failed:");
         message.append(NL);
         message.append(function);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertLogLinearOrPowerLaw(BigOAnalyser boa, String method) {
      // check preconditions
      Preconditions.checkNotNull(boa);
      Preconditions.checkNotNull(method);
      Preconditions.checkArgument(boa.isAnalysed(method), "here we need a analysed method name");
      
      // fetch measured data
      final Table<Integer, String, Double> data = boa.getResultTableChecked(method);

      // log-linear functions should have a polynomial degree of 1.1
      assertPolynomialDegree(boa, method, DEGREE_EXPECTED_LOGLINEAR, DEGREE_EXPECTED_DELTA);

      // find the best fit function and check type
      final String function = BigOReports.calculateBestFunction(data);
      if (!(function.startsWith("LogLinear") || function.startsWith("PowerLaw"))) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException - assertLogLinear failed:");
         message.append(NL);
         message.append(function);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertQuadratic(BigOAnalyser boa, String method) {
      // check preconditions
      Preconditions.checkNotNull(boa);
      Preconditions.checkNotNull(method);
      Preconditions.checkArgument(boa.isAnalysed(method), "here we need a analysed method name");
      
      // fetch measured data
      final Table<Integer, String, Double> data = boa.getResultTableChecked(method);

      // quadratic functions should have a polynomial degree of 2.0
      assertPolynomialDegree(boa, method, DEGREE_EXPECTED_QUADRATIC, DEGREE_EXPECTED_DELTA);

      // find the best fit function and check type
      final String function = BigOReports.calculateBestFunction(data);
      if (!function.startsWith("Quadratic")) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException - assertQuadratic failed:");
         message.append(NL);
         message.append(function);
         throw new BigOAssertWarningError(message.toString());
      }
   }

   public static void assertPowerLaw(BigOAnalyser boa, String method) {
      // check preconditions
      Preconditions.checkNotNull(boa);
      Preconditions.checkNotNull(method);
      Preconditions.checkArgument(boa.isAnalysed(method), "here we need a analysed method name");
      
      // fetch measured data
      final Table<Integer, String, Double> data = boa.getResultTableChecked(method);

      // find the best fit function and check type
      final String function = BigOReports.calculateBestFunction(data);
      if (!function.startsWith("PowerLaw")) {
         final StringBuilder message = new StringBuilder(100);
         message.append("BigOAssertException - assertPowerLaw failed:");
         message.append(NL);
         message.append(function);
         throw new BigOAssertWarningError(message.toString());
      }
   }
}
