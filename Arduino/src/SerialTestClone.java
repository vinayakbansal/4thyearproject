/**
 * 
 * @author Vinayak Bansal Dated Apr 3, 2014 
 */

// TODO Vinayak Bansal: Remove this file once we test on a real Arduino

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class SerialTestClone implements SerialPortEventListener {
	SerialPort serialPort;
	public List<Double> data;
	public List<Double> xaxis;
	/** The port we're normally going to use. */
	// Mac OS X
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1",
			"/dev/ttyUSB0", // Linux
			"COM4", // Windows
	};
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results code page independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() {
		data = new ArrayList<Double>();
		xaxis = new ArrayList<Double>();
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			serialPort.setRTS(true);
			serialPort.setDTR(true);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			output = serialPort.getOutputStream();
			System.out.println("te");
			output.write("60".getBytes());
			output.flush();

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				xaxis.add((double) System.currentTimeMillis());
				data.add(Double.parseDouble(inputLine));
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		SerialTestClone main = new SerialTestClone();
		main.initialize();
		System.out.println("Started");
		Thread t = new Thread() {
			public void run() {
				// the following line will keep this app alive for 1000 seconds,
				// waiting for events to occur and responding to them (printing
				// incoming messages to console).
				try {
					Thread.sleep(10000);
				} catch (InterruptedException ie) {
				}
			}
		};
		t.start();
		t.join();
		main.close();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("abc.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Double a : main.data) {
			pw.println(a);
		}
		pw.close();

		Plot2DPanel plot = new Plot2DPanel();

		// add a line plot to the PlotPanel
		double[] x = new double[main.xaxis.size()];
		double[] y = new double[main.data.size()];
		double[] der = new double[main.data.size()];
		for (int a = 0; a < main.xaxis.size(); a++) {
			x[a] = main.xaxis.get(a) - time;
			y[a] = main.data.get(a);
			if (a != 0) {
				double val = (y[a] - y[a - 1]) * 1000 / (x[a] - x[a - 1]);
				if (val > 0) {
					der[a] = 1023;
				} else if (val < 0) {
					der[a] = 0;
				} else {
					der[a] = 512;
				}
			}

		}
		plot.addLinePlot("my plot", x, y);
		plot.addLinePlot("my plot", x, der);

		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setContentPane(plot);
		frame.setSize(400, 400);
		frame.setVisible(true);
		Thread.sleep(2000);
		// plot.removeAllPlots();
		// plot.addLinePlot("my plot", y, x);

	}
}