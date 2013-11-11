import java.util.List;

import com.sw_engineering_candies.big_o_test.interfaces.BigOParameter;

public class BubbleSort {

   public Long[] sort(@BigOParameter List<Long> list) {

      final Long[] result = new Long[list.size()];

      // Remove the smallest value from the heap until the heap is empty
      for (int i = 0; i < list.size(); i++) {
         result[i] = list.get(i);
      }
      bubbleSort(result);

      return result;
   }

   private static void bubbleSort(final Long[] result) {
      int length = result.length;
      boolean changed = false;
      do {
         changed = false;
         for (int i = 0; i < length - 1; i++) {
            if (result[i] > result[i + 1]) {
               final long temp = result[i + 1];
               result[i + 1] = result[i];
               result[i] = temp;
               changed = true;
            }
         }
         length -= 1;
      } while (changed && length > 1);
   }
}