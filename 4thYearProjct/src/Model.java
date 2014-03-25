/**
 * Author: Vinayak Bansal
 * Mar 24, 2014
 */

import java.util.ArrayList;
import java.util.List;

public class Model {
	private List<Double> data;
	private List<Double> xaxis;
	private long startTime;

	public Model() {
		startTime = System.currentTimeMillis();
		data = new ArrayList<Double>();
		xaxis = new ArrayList<Double>();
	}

	public synchronized void addValue(double input) {
		data.add(input);
		xaxis.add((double) (System.currentTimeMillis() - startTime));
	}

	public synchronized double[] getValues() {
		// TODO (Vinayak Bansal): Do not hardcode the 500
		int max = Math.max(500, data.size());
		double[] toReturn = new double[max];
		for (int x = 0; x < max; x++) {
			toReturn[x] = data.get(x);
		}
		return toReturn;
	}

}
