/**
 * 
 * @author Vinayak Bansal Dated Apr 3, 2014 
 */

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class Plot {
	Plot2DPanel plot;
	JFrame frame;

	public Plot() {
		plot = new Plot2DPanel();
		frame = new JFrame("a plot panel");
		frame.setContentPane(plot);
		frame.setSize(400, 400);
		frame.setVisible(true);

	}

	public void refresh(double[] x, double[][] y) throws InterruptedException {
		// add a line plot to the PlotPanel
		plot.removeAllPlots();
		for (int a = 0; a < y.length; a++) {
			plot.addLinePlot("my plot", x, y[a]);
		}
	}
}