/*
 * Copyright (C) 2013, Markus Sprunck <sprunck.markus@gmail.com>
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
package com.sw_engineering_candies.big_o_test.tests;

import java.util.List;

import com.sw_engineering_candies.big_o_test.BigOParameter;

public class Algorithms {

	public double run(@BigOParameter @DummyAnnotation List<Integer> m,
			@DummyAnnotation boolean flag, @BigOParameter int[] n, @BigOParameter float[] k) {
		double result = 0;
		for (final Integer value_m : m) {
			for (final int value_n : n) {
				for (final float value_k : k) {
					result += value_m * value_n * value_k;
				}
			}
		}
		return result;
	}

	public double runConstant(@BigOParameter int m) {
		double result = 0;
		for (int index = 0; index < 10; index++) {
			result += index;
		}
		return result;
	}

	public double runLinear(@BigOParameter int m) {
		double result = 0;
		for (int index = 0; index < m; index++) {
			result += index;
		}
		return result;
	}

	public double runQuadratic(@BigOParameter int m) {
		double result = 0;
		for (int index = 0; index < m; index++) {
			for (int index2 = 0; index2 < m; index2++) {
				result += index + index2;
			}
		}
		return result;
	}

	public double runNLogN(@BigOParameter int m) {
		double result = 0;
		final long logN = Math.round(Math.log(m));
		for (int index = 0; index < m; index++) {
			for (long index2 = 0; index2 < logN; index2++) {
				result += index + index2;
			}
		}
		return result;
	}

}