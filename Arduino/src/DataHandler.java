
public class DataHandler implements IHandleData{
	private Model model;
	private Plot p;
	
	public DataHandler() {
		model = new Model();
		p = new Plot();
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
		double[] der = new double[values[0].length];
		for (int a = 0; a < values[0].length; a++) {
			if (a != 0) {
				double val = (values[1][a] - values[1][a - 1]) * 1000
						/ (values[0][a] - values[0][a - 1]);
				if (val > 0) {
					der[a] = 1023;
				} else if (val < 0) {
					der[a] = 0;
				} else {
					der[a] = 512;
				}
			}

		}
		double[][] data = new double[2][values[0].length];
		data[0] = values[1];
		data[1] = der;
		try {
			p.refresh(values[0], data);
		} catch (InterruptedException e) {
		}

	}

}
