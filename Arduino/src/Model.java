/**
 * 
 * @author Vinayak Bansal Dated Apr 3, 2014 
 */

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class Model {
	private long startTime;
	public static int MAX = 500;
	private CircularFifoQueue<Double> data;
	private CircularFifoQueue<Double> xaxis;

	public Model() {
		startTime = System.currentTimeMillis();
		data = new CircularFifoQueue<Double>(MAX);
		xaxis = new CircularFifoQueue<Double>(MAX);
	}

	public synchronized void addValue(double input) {
		xaxis.add((double) (System.currentTimeMillis() - startTime));
		data.add(input);
	}

	public synchronized double[][] getValues() {
		double[][] toReturn = new double[2][MAX];
		for (int x = 0; x < data.size(); x++) {
			toReturn[0][x] = xaxis.get(x);
			toReturn[1][x] = data.get(x);
		}
		return toReturn;
	}

	public static void main(String args[]) {
		Model m = new Model();
		for (int x = 0; x < MAX; x++) {
			m.addValue(x);
		}
		System.out.println(m.getValues());
		m.addValue(500);
		m.addValue(501);
		m.addValue(502);
		System.out.println(m.getValues());

	}

}
