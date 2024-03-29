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

import sw_engineering_candies.assertBigO.interfaces.BigOParameter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("UnusedReturnValue")
public class Algorithms {

    public double run(@BigOParameter @DummyAnnotation List<Integer> m,
                      @DummyAnnotation boolean flag,
                      @BigOParameter int[] n,
                      @BigOParameter float[] k
    ) {
        double result = 0;
        if (flag) {
            for (final Integer value_m : m) {
                for (final int value_n : n) {
                    for (final float value_k : k) {
                        result += value_m * value_n * value_k;
                    }
                }
            }
        }
        return result;
    }

    public double runConstant(@BigOParameter int m) {
        double result = 0;
        for (int index = 0; index < 100; index++) {
            result += index;
        }
        return result + m;
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
        final long count = Math.round(Math.pow(m, 1.1));
        for (int index = 0; index < count; index++) {
            result += Math.sin(index);
        }
        return result;
    }

    public double runPowerLaw(@BigOParameter int m) {
        double result = 0;
        final long count = Math.round(Math.pow(m, 1.5));
        for (int index = 0; index < count; index++) {
            result += Math.sin(index);
        }
        return result;
    }

    public double runAllParameter(@BigOParameter int[] in01,
                                  @BigOParameter long[] in02,
                                  @BigOParameter float[] in03,
                                  @BigOParameter double[] in04,
                                  @BigOParameter byte[] in05,
                                  @BigOParameter String in06,
                                  @BigOParameter List<Integer> in07,
                                  @BigOParameter Set<Integer> in08,
                                  @BigOParameter Map<Integer, Integer> in09,
                                  @BigOParameter int in10,
                                  @BigOParameter long in11
    ) {
        double result = 0;
        for (int index = 0; index < 10; index++) {
            result += index;
        }
        return result + in01.length + in02.length + in03.length + in04.length + in05.length + in06.length() + in07.size() + in08.size() + in09.size() + in10 + in11;
    }

    public boolean runNotSupportedParameter(@BigOParameter File file) {
        return file.exists();
    }

}