package com.sw_engineering_candies.big_o_test;

import junit.framework.Assert;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.utils.Algorithms;

public class BigOAssertQuadraticTest {

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
		} catch (final IllegalArgumentException ex) {
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
		} catch (final BigOAssertException ex) {
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
		} catch (final BigOAssertException ex) {
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
		} catch (final BigOAssertException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertTrue(exceptionHappened);
	}

}
