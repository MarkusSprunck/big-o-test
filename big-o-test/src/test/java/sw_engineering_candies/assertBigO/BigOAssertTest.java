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

package sw_engineering_candies.assertBigO;

import com.google.common.collect.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static sw_engineering_candies.assertBigO.BigOAssert.*;


public class BigOAssertTest {

    @Test
    public void assertConstant_EmptyMethodString_RaiseIllegalArgumentException() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
               assertConstant(boa, "")
        );

        // then
        assertEquals("here we need an analysed method name", exception.getMessage());
    }

    @Test
    public void assertPolynomialDegree_runConstant() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);
        sut.runConstant(1024);
        sut.runConstant(512);
        sut.runConstant(256);
        sut.runConstant(128);

        // then
        assertDoesNotThrow( () ->
               assertPolynomialDegree(boa, "runConstant",  DEGREE_EXPECTED_CONSTANT, 3*DEGREE_EXPECTED_DELTA)
        );
    }

    @Test
    public void assertConstant_ThreeDataPoints_RaiseIllegalArgumentException() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
               assertConstant(boa, "runLinear")
        );

        // then
        assertEquals("here we need an analysed method name", exception.getMessage());
    }

    @Test
    public void assertConstant_RunConstant_DetectConstantIsOk() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);
        sut.runConstant(1024);
        sut.runConstant(512);
        sut.runConstant(256);

        // when
        assertDoesNotThrow(() ->
               assertConstant(boa, "runConstant")
        );
    }

    @Test
    public void assertConstant_RunNLogN_DetectConstantFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runNLogN(16384);
        sut.runNLogN(8192);
        sut.runNLogN(4096);
        sut.runNLogN(2048);
        sut.runNLogN(1024);
        sut.runNLogN(512);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
               assertQuadratic(boa, "runNLogN")
        );

        final Table<Integer, String, Double> data = boa.getData("runNLogN");
        BigOReports.getBestFunctionsReport(data);

        // then
        String expected = """
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 2.0
                \tPolynomial degree actual   = 1.""";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    public void assertConstant_RunLinear_DetectConstantFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);
        sut.runLinear(300);
        sut.runLinear(100);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
               assertConstant(boa, "runLinear")
        );

        // then
        String expected = """
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 0.0
                \tPolynomial degree actual   =""";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    public void assertLinear_ThreeDataPoints_RaiseIllegalArgumentException() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
               assertLinear(boa, "runLinear")
        );

        // then
        assertEquals("minimum 4 data points are needed for a reliable analysis", exception.getMessage());
    }

    @Test
    public void assertLinear_RunConstant_DetectLinearFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(10000);
        sut.runConstant(3000);
        sut.runConstant(1000);
        sut.runConstant(300);
        sut.runConstant(100);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
               assertLinear(boa, "runConstant")
        );

        // then
        String expected = """
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.0
                \tPolynomial degree actual   =""";
        assertTrue(exception.getMessage().startsWith(expected));
    }

    @Test
    public void assertLinear_RunQuadratic_DetectLinearFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(10000);
        sut.runQuadratic(3000);
        sut.runQuadratic(1000);
        sut.runQuadratic(300);
        sut.runQuadratic(100);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
               assertLinear(boa, "runQuadratic")
        );

        // then
        String expected = """
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.0
                \tPolynomial degree actual   =""";
        assertTrue(exception.getMessage().startsWith(expected));
    }

    @Test
    public void assertLinear_RunLinear_DetectLinearIsOk() {
        // given
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

        // when
        assertDoesNotThrow(() ->
               assertLinear(boa, "runLinear")
        );
        final Table<Integer, String, Double> data = boa.getData("runLinear");
        String result = BigOReports.getBestFunctionsReport(data);

        // then
        assertTrue(result.contains("TYPE       \tR^2 (adjusted)\tFUNCTION"));
        assertTrue(result.contains("Linear    \t"));
        assertTrue(result.contains("Exponential\t"));
        assertTrue(result.contains("Logarithmic\t"));
    }

    @Test
    public void assertLogLinear_ThreeDataPoints_RaiseIllegalArgumentException() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runNLogN(10000);
        sut.runNLogN(3000);
        sut.runNLogN(1000);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
               assertLinear(boa, "runNLogN")
        );

        // then
        assertEquals("minimum 4 data points are needed for a reliable analysis", exception.getMessage());
    }


    @Test
    public void assertLogLinear_RunNLogN2_DetectLogLinearOk() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);

        // when
        for (int x = (128 * 1024); x >= 32; x /= 2) {
            sut.runNLogN(x);
        }

        // then
        assertLogLinearOrPowerLaw(boa, "runNLogN");
        assertLogLinear(boa, "runNLogN");
        assertPolynomialDegree(boa, "runNLogN", DEGREE_EXPECTED_LOG_LINEAR, DEGREE_EXPECTED_DELTA);
    }

    @Test
    public void assertLogLinear_RunNLogN_DetectLogLinearOk() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        boa.deactivate();                      // measurement is deactivated
        sut.runNLogN(256 * 1024);           // give JIT compiler the chance to optimize
        boa.activate();                        // measurement is active

        // when
        for (int x = (1024 * 1024); x >= 4; x /= 2) {
            sut.runNLogN(x);
        }

        // then
       assertLogLinearOrPowerLaw(boa, "runNLogN");
       assertLogLinear(boa, "runNLogN");

        assertTrue(BigOReports.getPolynomialDegree(boa.getData("runNLogN")).contains("ESTIMATED-POLYNOMIAL-DEGREE\n" + "1.1"));
    }

    @Test
    public void assertLogLinear_RunQuadratic_DetectLinearFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(16384);
        sut.runQuadratic(8192);
        sut.runQuadratic(4096);
        sut.runQuadratic(2048);
        sut.runQuadratic(1024);
        sut.runQuadratic(512);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class,
                () ->assertLogLinearOrPowerLaw(boa, "runQuadratic")
        );

        // then
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.1
                \tPolynomial degree actual   =\s"""));
    }

    @Test
    public void assertLogLinear_RunConstant_DetectLinearFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(16384);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);
        sut.runConstant(1024);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class,
                () ->assertLogLinearOrPowerLaw(boa, "runConstant")
        );

        // then
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.1
                \tPolynomial degree actual   =\s"""));
    }

    @Test
    public void assertPolynomialDegree_ThreeDataPoints_RaiseIllegalArgumentException() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () ->assertPolynomialDegree(boa, "runLinear", 1.0, 0.1)
        );

        // then
        assertEquals("minimum 4 data points are needed for a reliable analysis", exception.getMessage());
    }

    @Test
    public void assertPolynomialDegree_RunLinear_CheckPolynomialDegreeIsOk() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(100000);
        sut.runLinear(30000);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);
        sut.runLinear(300);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class,
                () ->assertPolynomialDegree(boa, "runLinear", 1.0, 0.0)
        );

        // then
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.0
                \tPolynomial degree actual   ="""));
    }

    @Test
    public void assertPolynomialDegree_RunLinear_CheckPolynomialDegreeIsSmaller() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(10000);
        sut.runConstant(3000);
        sut.runConstant(1000);
        sut.runConstant(300);
        sut.runConstant(100);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class,
                () ->assertPolynomialDegree(boa, "runConstant", 1.0, 0.1)
        );

        // then
        String expected = """
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.0
                \tPolynomial degree actual   =""";
        assertTrue(exception.getMessage().startsWith(expected));
    }

    @Test
    public void assertPolynomialDegree_RunQuadratic_CheckPolynomialDegreeOk() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(10000);
        sut.runQuadratic(3000);
        sut.runQuadratic(1000);
        sut.runQuadratic(300);
        sut.runQuadratic(100);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
               assertPolynomialDegree(boa, "runQuadratic", 1.0, 0.1)
        );

        // then
        assertTrue(exception.getMessage().startsWith("""
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 1.0
                \tPolynomial degree actual   =\s"""));
    }

    @Test
    public void assertQuadratic_ThreeDataPoints_RaiseIllegalArgumentException() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(10000);
        sut.runQuadratic(3000);
        sut.runQuadratic(1000);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
               assertQuadratic(boa, "runQuadratic")
        );

        // then
        assertEquals("minimum 4 data points are needed for a reliable analysis", exception.getMessage());
    }

    @Test
    public void assertQuadratic_RunConstant_DetectQuadraticIsOk() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runQuadratic(16384);
        sut.runQuadratic(8192);
        sut.runQuadratic(4096);
        sut.runQuadratic(2048);
        sut.runQuadratic(1024);

        // then
        assertDoesNotThrow(() ->
               assertQuadratic(boa, "runQuadratic")
        );
    }

    @Test
    public void assertQuadratic_RunLinear_DetectQuadraticFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runLinear(30000);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);
        sut.runLinear(300);
        sut.runLinear(100);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
               assertQuadratic(boa, "runLinear")
        );

        // then
        String expected = """
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 2.0
                \tPolynomial degree actual   =""";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    public void assertPolynomialDegree_RunPowerLaw_DetectOK() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runPowerLaw(10000);
        sut.runPowerLaw(3000);
        sut.runPowerLaw(1000);
        sut.runPowerLaw(300);
        sut.runPowerLaw(100);

        // when
       assertPolynomialDegree(boa, "runPowerLaw", 1.7, 0.2);
    }

    @Test
    public void assertQuadratic_RunConstant_DetectQuadraticFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runConstant(16384);
        sut.runConstant(8192);
        sut.runConstant(4096);
        sut.runConstant(2048);
        sut.runConstant(1024);
        sut.runConstant(512);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
               assertQuadratic(boa, "runConstant")
        );

        // then
        String expected = """
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 2.0
                \tPolynomial degree actual   =""";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    public void assertQuadratic_RunNLogN_DetectQuadraticFailedAsExpected() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final Algorithms sut = (Algorithms) boa.createProxy(Algorithms.class);
        sut.runNLogN(16384);
        sut.runNLogN(8192);
        sut.runNLogN(4096);
        sut.runNLogN(2048);
        sut.runNLogN(1024);
        sut.runNLogN(512);

        // when
        BigOAssertWarningError exception = assertThrows(BigOAssertWarningError.class, () ->
               assertQuadratic(boa, "runNLogN")
        );

        // then
        String expected = """
                BigOAssertException - assertPolynomialDegree failed:
                \tPolynomial degree expected = 2.0
                \tPolynomial degree actual   =""";
        assertTrue(exception.getMessage().contains(expected));
    }

}
