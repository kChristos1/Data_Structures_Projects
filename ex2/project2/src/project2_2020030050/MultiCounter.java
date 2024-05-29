package project2_2020030050;

// using MultiCounter class as it is, no changes, from tutorial 3 (countingdemo eclipse project, org.tuc.counter package)

/**
 * A class with static member variables and methods that can be used to count
 * multiple stuff. safe.
 * 
 * @author sk
 *
 */
public class MultiCounter {

	/**
	 * variable holding our counters. We support up to 15 counters
	 */
	private static long[] counters = new long[15];

	/**
	 * Resets the internal counter of counterIndex to zero
	 */
	public static void resetCounter(int counterIndex) {
		if (counterIndex - 1 < counters.length)
			counters[counterIndex - 1] = 0;
	}

	/**
	 * Returns the current count for given counterIndex
	 * 
	 * @return the current count for given counterIndex
	 */
	public static long getCount(int counterIndex) {
		if (counterIndex - 1 < counters.length)
			return counters[counterIndex - 1];
		return -1;
	}

	/**
	 * Increases the current count of counterIndex by 1. Returns always true so that
	 * it can be used in boolean statements
	 * 
	 * @return always true
	 */
	public static boolean increaseCounter(int counterIndex) {
		if (counterIndex - 1 < counters.length)
			counters[counterIndex - 1]++;
		return true;
	}

	/**
	 * Increases the current count of counter given by counterIndex by step. Returns
	 * always true so that it can be used in boolean statements. Step could be
	 * negative. It is up to the specific usage scenario whether this is desirable
	 * or not.
	 * 
	 * @param step The amount to increase the counter
	 * @return always true
	 */
	public static boolean increaseCounter(int counterIndex, int step) {
		if (counterIndex - 1 < counters.length)
			counters[counterIndex] = counters[counterIndex - 1] + step;
		return true;
	}
}