package com.sw_engineering_candies.big_o_test;

import junit.framework.Assert;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.test_utils.Algorithms;

public class BigOAssertLinearTest {

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
		} catch (final IllegalArgumentException ex) {
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
		} catch (final BigOAssertException ex) {
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
		} catch (final BigOAssertException ex) {
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
		sut.runLinear(10000);
		sut.runLinear(3000);
		sut.runLinear(1000);
		sut.runLinear(300);
		sut.runLinear(100);

		// ACT
		boolean exceptionHappened = false;
		try {
			BigOAssert.assertLinear(bom, "runLinear");
		} catch (final BigOAssertException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertFalse(exceptionHappened);
	}

}
