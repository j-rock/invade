package com.fourthrock.invade.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class ClockTest {

	@Test
	public void clockResetsProperly() {
		final long start = System.currentTimeMillis();
		final Clock clock = new Clock();
		final long timePassed = clock.reset();
		final long end = System.currentTimeMillis();
		
		assertTrue(end - start >= timePassed);
	}
	
	@Test
	public void clockResumesProperly() throws InterruptedException {
		final long sleepTime = 50;
		final long start = System.currentTimeMillis();
		final Clock clock = new Clock();
		Thread.sleep(sleepTime);
		clock.resume();
		final long timePassed = clock.reset();
		final long end = System.currentTimeMillis();
		
		assertTrue(end - start >= timePassed);
		assertTrue(timePassed < sleepTime);
	}
}
