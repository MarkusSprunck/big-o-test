package com.sw_engineering_candies.big_o_test;

import junit.framework.Assert;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.utils.Algorithms;

public class BigOAssertLogLinearTest {

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
			BigOAssert.assertLogLinear(bom, "runNLogN");
		} catch (final IllegalArgumentException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertTrue(exceptionHappened);
	}

	@Test
	public void assertLogLinear_RunNLogN_DetectLinearIsOk() {

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
			BigOAssert.assertLogLinear(bom, "runNLogN");
		} catch (final BigOAssertException ex) {
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
			BigOAssert.assertLogLinear(bom, "runQuadratic");
		} catch (final BigOAssertException ex) {
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
		sut.runConstant(512);

		// ACT
		boolean exceptionHappened = false;
		try {
			BigOAssert.assertLogLinear(bom, "runConstant");
		} catch (final BigOAssertException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertTrue(exceptionHappened);
	}

}
