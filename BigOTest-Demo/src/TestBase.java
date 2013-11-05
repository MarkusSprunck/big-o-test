import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.internal.Reports;

public class TestBase {

	public static List<Integer> createSortInput(int size) {
		final List<Integer> result = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			result.add((int) Math.round(Integer.MAX_VALUE * Math.random()));
		}
		return result;
	}

	public static void traceReport(final BigOAnalyser bom, String method, String title) {
		System.out.println(title);
		Table<Integer, String, Double> resultTable = bom.getResultTable(method);
		System.out.println(Reports.createFullReport(resultTable));
	}

}