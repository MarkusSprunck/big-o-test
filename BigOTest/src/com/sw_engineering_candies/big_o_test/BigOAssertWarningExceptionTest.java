package com.sw_engineering_candies.big_o_test;

import junit.framework.Assert;

import org.junit.Test;

public class BigOAssertWarningExceptionTest {

	@Test
	public void testBigOAssertWarningException() {
		// ARRANGE
		final BigOAssertWarningException sut = new BigOAssertWarningException("my Message");

		// ACT
		final String result = sut.getMessage();

		// ASSERT
		Assert.assertEquals("my Message", result);
	}

}
