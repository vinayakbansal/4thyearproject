import org.apache.commons.math3.transform.DctNormalization;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * 
 * @author Vinayak Bansal Dated Apr 3, 2014
 * 
 */

public class DataHandler implements IHandleData {
	private Model model;
	private Plot p;
	private Plot p2;
	private double[] xaxis;

	public DataHandler() {
		model = new Model();
		p = new Plot();
		p2 = new Plot();
		xaxis = new double[Model.MAX];
		for (int x = 0; x < Model.MAX; x++) {
			xaxis[x] = x;
		}
	}

	public static void main(String[] args) {
		DataHandler dataHandler = new DataHandler();
		new MockArduino(dataHandler);
	}

	@Override
	public void handleValue(double value) {
		model.addValue(value);
		refresh();
	}

	private void refresh() {
		double[][] values = model.getValues();
		// double[] der = new double[values[0].length];
		// for (int a = 0; a < values[0].length; a++) {
		// if (a != 0) {
		// double val = (values[1][a] - values[1][a - 1]) * 1000
		// / (values[0][a] - values[0][a - 1]);
		// if (val > 0) {
		// der[a] = 100;
		// } else if (val < 0) {
		// der[a] = 0;
		// } else {
		// der[a] = 50;
		// }
		// }
		//
		// }
		double[][] timeData = new double[2][values[0].length];
		timeData[0] = values[1];

		double[][] frequencyData = new double[2][values[0].length];
		frequencyData[0] = values[1];
		frequencyData[1] = model.getFFT();

		double[] temp = new double[Model.MAX];

		for (int x = 0; x < Model.MAX; x++) {
			if (frequencyData[1][x] < 30000) {
				temp[x] = 0;
			} else {
				temp[x] = timeData[1][x];
			}
		}

		timeData[1] = new FastCosineTransformer(DctNormalization.STANDARD_DCT_I)
				.transform(temp, TransformType.INVERSE);
		try {
			p.refresh(values[0], timeData);
			p2.refresh(xaxis, frequencyData);
		} catch (InterruptedException e) {
		}

	}

}
