package com.sw_engineering_candies.big_o_test.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;

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
			exceptionHappened = true;
		}

		// ASSERT
		Assert.assertTrue(exceptionHappened);
	}

}
