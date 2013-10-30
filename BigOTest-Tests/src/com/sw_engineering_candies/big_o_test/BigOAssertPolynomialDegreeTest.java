package com.sw_engineering_candies.big_o_test;

import junit.framework.Assert;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOAssertException;
import com.sw_engineering_candies.big_o_test.utils.Algorithms;

public class BigOAssertPolynomialDegreeTest {

	@Test
	public void estimatePolynomialDegree_ThreeDataPoints_RaiseIllegalArgumentException() {

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
		} catch (final IllegalArgumentException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertTrue(exceptionHappened);
	}

	@Test
	public void estimatePolynomialDegree_RunLinear_CheckPolynomialDegreeIsOk() {

		// ARRANGE
		final BigOAnalyser bom = new BigOAnalyser();
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
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
		} catch (final BigOAssertException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertFalse(exceptionHappened);
	}

	@Test
	public void estimatePolynomialDegree_RunLinear_CheckPolynomialDegreeIsSmaller() {

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
		} catch (final BigOAssertException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertTrue(exceptionHappened);
	}

	@Test
	public void estimatePolynomialDegree_RunLinear_CheckPolynomialDegreeIsLargerr() {

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
		} catch (final BigOAssertException ex) {
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertTrue(exceptionHappened);
	}

}
