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

import com.google.common.collect.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BigOAssertTest {

    @Test
    public void assertConstant_EmptyMethodString_RaiseIllegalArgumentException() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);

        // ACT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                BigOAssert.assertConstant(boa, "")
        );

        // ASSERT
        assertEquals("here we need an analysed method name", exception.getMessage());
    }

    @Test
    public void assertConstant_ThreeDataPoints_RaiseIllegalArgumentException() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);

        // ACT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                BigOAssert.assertConstant(boa, "runLinear")
        );

        // ASSERT
        assertEquals("here we need an analysed method name", exception.getMessage());
    }

    @Test
    public void assertConstant_RunConstant_DetectConstantIsOk() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        boa.deactivate();
        sut.runConstant(32768);
        boa.activate();
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);
        sut.runConstant(1024);

        // ACT
        assertDoesNotThrow(() ->
                BigOAssert.assertConstant(boa, "runConstant")
        );

    }

    @Test
    public void assertConstant_RunNLogN_DetectConstantFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runNLogN(10000);
        sut.runNLogN(3000);
        sut.runNLogN(1000);
        sut.runNLogN(300);
        sut.runNLogN(100);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertConstant(boa, "runNLogN");
        } catch (final BigOAssertWarningError ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

    @Test
    public void assertConstant_RunLinear_DetectConstantFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);
        sut.runLinear(300);
        sut.runLinear(100);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertConstant(boa, "runLinear");
        } catch (final BigOAssertWarningError ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

    @Test
    public void assertLinear_ThreeDataPoints_RaiseIllegalArgumentException() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertLinear(boa, "runLinear");
        } catch (final IllegalStateException ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

    @Test
    public void assertLinear_RunConstant_DetectLinearFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(10000);
        sut.runConstant(3000);
        sut.runConstant(1000);
        sut.runConstant(300);
        sut.runConstant(100);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertLinear(boa, "runConstant");
        } catch (final BigOAssertWarningError ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

    @Test
    public void assertLinear_RunQuadratic_DetectLinearFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(10000);
        sut.runQuadratic(3000);
        sut.runQuadratic(1000);
        sut.runQuadratic(300);
        sut.runQuadratic(100);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertLinear(boa, "runQuadratic");
        } catch (final BigOAssertWarningError ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

    @Test
    public void assertLinear_RunLinear_DetectLinearIsOk() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        boa.deactivate();
        sut.runLinear(1000000);
        boa.activate();
        sut.runLinear(1000000);
        sut.runLinear(300000);
        sut.runLinear(100000);
        sut.runLinear(30000);
        sut.runLinear(10000);
        sut.runLinear(3000);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertLinear(boa, "runLinear");
        } catch (final BigOAssertWarningError ex) {
            final Table<Integer, String, Double> data = boa.getData("runLinear");
            System.out.println(ex.getMessage());
            System.out.println(BigOReports.getBestFunctionsReport(data));
            exceptionHappened = true;
        }

        // ASSERT
        assertFalse(exceptionHappened);
    }

    @Test
    public void assertLogLinear_ThreeDataPoints_RaiseIllegalArgumentException() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runNLogN(10000);
        sut.runNLogN(3000);
        sut.runNLogN(1000);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertLogLinearOrPowerLaw(boa, "runNLogN");
        } catch (final IllegalStateException ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

    /*
    @Test
    public void assertLogLinear_RunNLogN2_DetectLogLinearOk() {
        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);

        boa.deactivate();                         // measurement is deactivated
        sut.runNLogN(1024 * 1024);               // give JIT compiler the chance to optimize
        boa.activate();                           // measurement is active

        // ACT
        for (int x = (128 * 1024); x >= 32; x /= 2) {
            sut.runNLogN(x);
        }

        // ASSERT
        BigOAssert.assertLogLinearOrPowerLaw(boa, "runNLogN");
    }

     */

    @Test
    public void assertLogLinear_RunNLogN_DetectLogLinearOk() {
        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        boa.deactivate();                      // measurement is deactivated
        sut.runNLogN(256 * 1024);           // give JIT compiler the chance to optimize
        boa.activate();                        // measurement is active

        // ACT
        for (int x = (1024 * 1024); x >= 4; x /= 2) {
            sut.runNLogN(x);
        }

        // ASSERT
        BigOAssert.assertLogLinearOrPowerLaw(boa, "runNLogN");
        BigOAssert.assertLogLinear(boa, "runNLogN");
        System.out.println(BigOReports.getPolynomialDegree(boa.getData("runNLogN")));
        System.out.println(BigOReports.getBestFunctionsReport(boa.getData("runNLogN")));
    }

    @Test
    public void assertLogLinear_RunQuadratic_DetectLinearFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(16384);
        sut.runQuadratic(8192);
        sut.runQuadratic(4096);
        sut.runQuadratic(2048);
        sut.runQuadratic(1024);
        sut.runQuadratic(512);

        // ACT
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class,
                () -> BigOAssert.assertLogLinearOrPowerLaw(boa, "runQuadratic")
        );

        // ASSERT
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.1
                \tPolynomial degree actual   =\s"""));
    }

    @Test
    public void assertLogLinear_RunConstant_DetectLinearFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(16384);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);
        sut.runConstant(1024);

        // ACT
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class,
                () -> BigOAssert.assertLogLinearOrPowerLaw(boa, "runConstant")
        );

        // ASSERT
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.1
                \tPolynomial degree actual   =\s"""));
    }

    @Test
    public void assertPolynomialDegree_ThreeDataPoints_RaiseIllegalArgumentException() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);

        // ACT
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> BigOAssert.assertPolynomialDegree(boa, "runLinear", 1.0, 0.1)
        );

        // ASSERT
        assertEquals("minimum 4 data points are needed for a reliable analysis", exception.getMessage());
    }

    @Test
    public void assertPolynomialDegree_RunLinear_CheckPolynomialDegreeIsOk() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(100000);
        sut.runLinear(30000);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);
        sut.runLinear(300);

        // ACT
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class,
                () -> BigOAssert.assertPolynomialDegree(boa, "runLinear", 1.0, 0.0001)
        );

        // ASSERT
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.0
                \tPolynomial degree actual   ="""));
    }

    @Test
    public void assertPolynomialDegree_RunLinear_CheckPolynomialDegreeIsSmaller() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(10000);
        sut.runConstant(3000);
        sut.runConstant(1000);
        sut.runConstant(300);
        sut.runConstant(100);

        // ACT
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class,
                () -> BigOAssert.assertPolynomialDegree(boa, "runConstant", 1.0, 0.1)
        );

        // ASSERT
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.0
                \tPolynomial degree actual   =\s"""));
    }

    @Test
    public void assertPolynomialDegree_RunQuadratic_CheckPolynomialDegreeOk() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(10000);
        sut.runQuadratic(3000);
        sut.runQuadratic(1000);
        sut.runQuadratic(300);
        sut.runQuadratic(100);

        // ACT
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
                BigOAssert.assertPolynomialDegree(boa, "runQuadratic", 1.0, 0.1)
        );

        // ASSERT
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.0
                \tPolynomial degree actual   =\s"""));
    }

    @Test
    public void assertQuadratic_ThreeDataPoints_RaiseIllegalArgumentException() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(10000);
        sut.runQuadratic(3000);
        sut.runQuadratic(1000);

        // ACT
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                BigOAssert.assertQuadratic(boa, "runQuadratic")
        );

        // ASSERT
        assertEquals("minimum 4 data points are needed for a reliable analysis", exception.getMessage());
    }

    @Test
    public void assertQuadratic_RunConstant_DetectQuadraticIsOk() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(16384);
        sut.runQuadratic(8192);
        sut.runQuadratic(4096);
        sut.runQuadratic(2048);
        sut.runQuadratic(1024);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertQuadratic(boa, "runQuadratic");
        } catch (final BigOAssertWarningError ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertFalse(exceptionHappened);
    }

    @Test
    public void assertQuadratic_RunLinear_DetectQuadraticFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(30000);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);
        sut.runLinear(300);
        sut.runLinear(100);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertQuadratic(boa, "runLinear");
        } catch (final BigOAssertWarningError ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

    @Test
    public void assertPolynomialDegree_RunPowerLaw_DetectOK() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runPowerLaw(10000);
        sut.runPowerLaw(3000);
        sut.runPowerLaw(1000);
        sut.runPowerLaw(300);
        sut.runPowerLaw(100);

        // ACT
        BigOAssert.assertPolynomialDegree(boa, "runPowerLaw", 1.5, 0.2);
        BigOAssert.assertPowerLaw(boa, "runPowerLaw");

        // ASSERT
    }

    @Test
    public void assertQuadratic_RunConstant_DetectQuadraticFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(16384);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);
        sut.runConstant(1024);
        sut.runConstant(512);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertQuadratic(boa, "runConstant");
        } catch (final BigOAssertWarningError ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

    @Test
    public void assertQuadratic_RunNLogN_DetectQuadraticFailedAsExpected() {

        // ARRANGE
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runNLogN(16384);
        sut.runNLogN(8192);
        sut.runNLogN(4096);
        sut.runNLogN(2048);
        sut.runNLogN(1024);
        sut.runNLogN(512);

        // ACT
        boolean exceptionHappened = false;
        try {
            BigOAssert.assertQuadratic(boa, "runNLogN");
        } catch (final BigOAssertWarningError ex) {
            System.out.println(ex.getMessage());
            exceptionHappened = true;
        }

        // ASSERT
        assertTrue(exceptionHappened);
    }

}
