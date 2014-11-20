package com.fourthrock.invade.util;

/**
 * Simple class to keep track of how much time is passing
 * @author Joseph
 *
 */
public class Clock {
	private long lastCheckedTimeNanos;
	
	public Clock() {
		lastCheckedTimeNanos = System.nanoTime();
	}
	
	/**
	 * Returns the number of milliseconds that have passed
	 * since the clock was created or since the last reset.
	 */
	public long reset() {
		final long dt = System.nanoTime() - lastCheckedTimeNanos;
		lastCheckedTimeNanos += dt; //don't worry about overflow.
									 //it's not gonna happen for a long time.
		return dt / 1000000;
	}
	
	/**
	 * For resuming the clock if the app is {@code onPause()}
	 */
	public void resume() {
		lastCheckedTimeNanos = System.nanoTime() - 1;
	}
}
