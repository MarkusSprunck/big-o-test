package com.sw_engineering_candies.big_o_test.internal;

/**
 * Class to store measured results.
 */
public class Item {

	private int calls = 0;

	private long time = 0L;

	public long getTime() {
		return time;
	}

	public void addTime(long d) {
		calls++;
		time += d;
	}

	public int getCalls() {
		return calls;
	}

	public void setNanoTime(int d) {
		time = d;
	}

	public void setCalls(int calls) {
		this.calls = calls;
	}

}