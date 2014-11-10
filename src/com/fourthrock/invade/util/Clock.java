package com.fourthrock.invade.util;

/**
 * Simple class to keep track of how much time is passing
 * @author Joseph
 *
 */
public class Clock {
	private long lastCheckedTimeMillis;
	
	public Clock() {
		lastCheckedTimeMillis = System.currentTimeMillis();
	}
	
	/**
	 * Returns the number of milliseconds that have passed
	 * since the clock was created or since the last reset.
	 */
	public long reset() {
		final long dt = System.currentTimeMillis() - lastCheckedTimeMillis;
		lastCheckedTimeMillis += dt; //don't worry about overflow.
									 //it's not gonna happen for 290 million years.
		return dt;
	}
}
