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

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.utils.Algorithms;

public class BigOAssertTest {

   @Test
   public void assertConstant_EmptyMethodString_RaiseIllegalArgumentException() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runConstant(8192);
      sut.runConstant(4096);
      sut.runConstant(2048);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertConstant(bom, "");
      } catch (final IllegalArgumentException ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertConstant_ThreeDataPoints_RaiseIllegalArgumentException() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runConstant(8192);
      sut.runConstant(4096);
      sut.runConstant(2048);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertConstant(bom, "runLinear");
      } catch (final IllegalStateException ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertConstant_RunConstant_DetectConstantIsOk() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      bom.deactivate();
      sut.runConstant(32768);
      bom.activate();
      sut.runConstant(8192);
      sut.runConstant(4096);
      sut.runConstant(2048);
      sut.runConstant(1024);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertConstant(bom, "runConstant");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertFalse(exceptionHappened);
   }

   @Test
   public void assertConstant_RunNLogN_DetectConstantFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runNLogN(10000);
      sut.runNLogN(3000);
      sut.runNLogN(1000);
      sut.runNLogN(300);
      sut.runNLogN(100);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertConstant(bom, "runNLogN");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertConstant_RunLinear_DetectConstantFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runLinear(10000);
      sut.runLinear(3000);
      sut.runLinear(1000);
      sut.runLinear(300);
      sut.runLinear(100);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertConstant(bom, "runLinear");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertLinear_ThreeDataPoints_RaiseIllegalArgumentException() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runLinear(10000);
      sut.runLinear(3000);
      sut.runLinear(1000);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertLinear(bom, "runLinear");
      } catch (final IllegalStateException ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertLinear_RunConstant_DetectLinearFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runConstant(10000);
      sut.runConstant(3000);
      sut.runConstant(1000);
      sut.runConstant(300);
      sut.runConstant(100);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertLinear(bom, "runConstant");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertLinear_RunQuadratic_DetectLinearFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runQuadratic(10000);
      sut.runQuadratic(3000);
      sut.runQuadratic(1000);
      sut.runQuadratic(300);
      sut.runQuadratic(100);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertLinear(bom, "runQuadratic");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertLinear_RunLinear_DetectLinearIsOk() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      bom.deactivate();
      sut.runLinear(1000000);
      bom.activate();
      sut.runLinear(1000000);
      sut.runLinear(300000);
      sut.runLinear(100000);
      sut.runLinear(30000);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertLinear(bom, "runLinear");
      } catch (final BigOAssertWarningError ex) {
         final Table<Integer, String, Double> resultTable = bom.getResultTable("runLinear");
         System.err.println(ex.getMessage());
         System.out.println(BigOReports.caclulateBestFunctionsTable(resultTable));
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertFalse(exceptionHappened);
   }

   @Test
   public void assertLogLinear_ThreeDataPoints_RaiseIllegalArgumentException() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runNLogN(10000);
      sut.runNLogN(3000);
      sut.runNLogN(1000);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertLogLinearOrPowerLaw(bom, "runNLogN");
      } catch (final IllegalStateException ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertLogLinear_RunNLogN_DetectLogLinearOk() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runNLogN(16384);
      sut.runNLogN(8192);
      sut.runNLogN(4096);
      sut.runNLogN(2048);
      sut.runNLogN(1024);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertLogLinearOrPowerLaw(bom, "runNLogN");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertFalse(exceptionHappened);
   }

   @Test
   public void assertLogLinear_RunQuadratic_DetectLinearFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runQuadratic(16384);
      sut.runQuadratic(8192);
      sut.runQuadratic(4096);
      sut.runQuadratic(2048);
      sut.runQuadratic(1024);
      sut.runQuadratic(512);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertLogLinearOrPowerLaw(bom, "runQuadratic");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertLogLinear_RunConstant_DetectLinearFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runConstant(16384);
      sut.runConstant(8192);
      sut.runConstant(4096);
      sut.runConstant(2048);
      sut.runConstant(1024);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertLogLinearOrPowerLaw(bom, "runConstant");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertPolynomialDegree_ThreeDataPoints_RaiseIllegalArgumentException() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runLinear(10000);
      sut.runLinear(3000);
      sut.runLinear(1000);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertPolynomialDegree(bom, "runLinear", 1.0, 0.1);
      } catch (final IllegalStateException ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertPolynomialDegree_RunLinear_CheckPolynomialDegreeIsOk() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runLinear(100000);
      sut.runLinear(30000);
      sut.runLinear(10000);
      sut.runLinear(3000);
      sut.runLinear(1000);
      sut.runLinear(300);

      // ACT
      boolean exceptionHappened = false;
      try {
         final double expected = 1.0;
         final double range = 0.2;
         BigOAssert.assertPolynomialDegree(bom, "runLinear", expected, range);
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertFalse(exceptionHappened);
   }

   @Test
   public void assertPolynomialDegree_RunLinear_CheckPolynomialDegreeIsSmaller() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runConstant(10000);
      sut.runConstant(3000);
      sut.runConstant(1000);
      sut.runConstant(300);
      sut.runConstant(100);

      // ACT
      boolean exceptionHappened = false;
      try {
         final double expected = 1.0;
         final double range = 0.1;
         BigOAssert.assertPolynomialDegree(bom, "runConstant", expected, range);
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertPolynomialDegree_RunQuadratic_CheckPolynomialDegreeOk() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runQuadratic(10000);
      sut.runQuadratic(3000);
      sut.runQuadratic(1000);
      sut.runQuadratic(300);
      sut.runQuadratic(100);

      // ACT
      boolean exceptionHappened = false;
      try {
         final double expected = 1.0;
         final double range = 0.1;
         BigOAssert.assertPolynomialDegree(bom, "runQuadratic", expected, range);
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertQuadratic_ThreeDataPoints_RaiseIllegalArgumentException() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runQuadratic(10000);
      sut.runQuadratic(3000);
      sut.runQuadratic(1000);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertQuadratic(bom, "runQuadratic");
      } catch (final IllegalStateException ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertQuadratic_RunConstant_DetectQuadraticIsOk() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runQuadratic(16384);
      sut.runQuadratic(8192);
      sut.runQuadratic(4096);
      sut.runQuadratic(2048);
      sut.runQuadratic(1024);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertQuadratic(bom, "runQuadratic");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertFalse(exceptionHappened);
   }

   @Test
   public void assertQuadratic_RunLinear_DetectQuadraticFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runLinear(30000);
      sut.runLinear(10000);
      sut.runLinear(3000);
      sut.runLinear(1000);
      sut.runLinear(300);
      sut.runLinear(100);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertQuadratic(bom, "runLinear");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertQuadratic_RunConstant_DetectQuadraticFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runConstant(16384);
      sut.runConstant(8192);
      sut.runConstant(4096);
      sut.runConstant(2048);
      sut.runConstant(1024);
      sut.runConstant(512);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertQuadratic(bom, "runConstant");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

   @Test
   public void assertQuadratic_RunNLogN_DetectQuadraticFailedAsExpected() {

      // ARRANGE
      final BigOAnalyser bom = new BigOAnalyser();
      final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
      sut.runNLogN(16384);
      sut.runNLogN(8192);
      sut.runNLogN(4096);
      sut.runNLogN(2048);
      sut.runNLogN(1024);
      sut.runNLogN(512);

      // ACT
      boolean exceptionHappened = false;
      try {
         BigOAssert.assertQuadratic(bom, "runNLogN");
      } catch (final BigOAssertWarningError ex) {
         System.err.println(ex.getMessage());
         exceptionHappened = true;
      }

      // ASSERT
      Assert.assertTrue(exceptionHappened);
   }

}
