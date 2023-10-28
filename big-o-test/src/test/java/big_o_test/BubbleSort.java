package big_o_test;

import big_o_test.interfaces.BigOParameter;

import java.util.List;

public class BubbleSort {

    public Long[] sort(@BigOParameter List<Long> unsorted) {

        Long[] sorted = new Long[unsorted.size()];
        sorted = unsorted.toArray(sorted);

        int n = sorted.length;
        // iterate over the array comparing adjacent elements
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                //if elements not in order, swap them
                if (sorted[j] > sorted[j + 1]) {
                    long temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        return sorted;
    }

}