package com.sw_engineering_candies.big_o_test;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import com.sw_engineering_candies.big_o_test.test_utils.Algorithms;

public class BigOAssertWarningExceptionTest {

	@Test
	public void testBigOAssertWarningException() {
		// ARRANGE
		final BigOAssertWarningException sut = new BigOAssertWarningException("my Message");

		// ACT
		String result = sut.getMessage();

		// ASSERT
		Assert.assertEquals("my Message", result);
	}

}
