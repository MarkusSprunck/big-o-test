import java.util.List;

import com.sw_engineering_candies.big_o_test.BigOParameter;

public class BubbleSort {

	public Integer[] sort(@BigOParameter List<Integer> unsorted) {

		final Integer[] result = new Integer[unsorted.size()];

		// Remove the smallest value from the heap until the heap is empty
		for (int i = 0; i < unsorted.size(); i++) {
			result[i] = unsorted.get(i);
		}
		bubbleSort(result);

		return result;
	}

	private static void bubbleSort(final Integer[] result) {
		int length = result.length;
		boolean changed = false;
		do {
			changed = false;
			for (int i = 0; i < length - 1; i++) {
				if (result[i] > result[i + 1]) {
					final int temp = result[i + 1];
					result[i + 1] = result[i];
					result[i] = temp;
					changed = true;
				}
			}
			length -= 1;
		} while (changed && length > 1);
	}
}