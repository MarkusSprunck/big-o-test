package com.sw_engineering_candies.big_o_test;

import junit.framework.Assert;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.test_utils.Algorithms;

public class BigOAssertConstantTest {

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
		} catch (final IllegalArgumentException ex) {
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
		sut.runConstant(16384);
		sut.runConstant(8192);
		sut.runConstant(4096);
		sut.runConstant(2048);
		sut.runConstant(1024);

		// ACT
		boolean exceptionHappened = false;
		try {
			BigOAssert.assertConstant(bom, "runConstant");
		} catch (final BigOAssertException ex) {
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
		} catch (final BigOAssertException ex) {
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
		} catch (final BigOAssertException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertTrue(exceptionHappened);
	}

}
