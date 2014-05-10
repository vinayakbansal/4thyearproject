import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.math3.transform.DctNormalization;
import org.apache.commons.math3.transform.DstNormalization;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.FastSineTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * 
 * @author Vinayak Bansal Dated Apr 3, 2014 
 */

public class Model {
	private long startTime;
	public static int MAX = 513;
	private CircularFifoQueue<Double> data;
	private CircularFifoQueue<Double> xaxis;
	FastCosineTransformer abc;
	FastSineTransformer def;

	public Model() {
		startTime = System.currentTimeMillis();
		data = new CircularFifoQueue<Double>(MAX);
		xaxis = new CircularFifoQueue<Double>(MAX);
		abc = new FastCosineTransformer(DctNormalization.STANDARD_DCT_I);
		def = new FastSineTransformer(DstNormalization.STANDARD_DST_I);
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

	private synchronized double[] getCFT() {
		double[] toReturn = new double[MAX];
		for (int x = 0; x < data.size(); x++) {
			toReturn[x] = xaxis.get(x);
			toReturn[x] = data.get(x);
		}
		return abc.transform(toReturn, TransformType.FORWARD);
	}

	private synchronized double[] getSFT() {
		double[] toReturn = new double[MAX];
		for (int x = 0; x < data.size(); x++) {
			toReturn[x] = xaxis.get(x);
			toReturn[x] = data.get(x);
		}
		return def.transform(toReturn, TransformType.FORWARD);
	}

	public synchronized double[] getFFT() {
		double[] toReturn = new double[MAX];
		// double[] sine = getSFT();
		double[] cosine = getCFT();

		for (int x = 0; x < MAX; x++) {
//			toReturn[x] = Math.abs(cosine[x]);
		}

		return cosine;

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
