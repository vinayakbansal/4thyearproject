/**
 * 
 * @author Vinayak Bansal Dated Apr 3, 2014 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MockArduino {

	public MockArduino(final IHandleData abc) {
		new Thread() {
			public void run() {
				List<Double> data = new ArrayList<Double>();
				Scanner hotdog = null;
				try {
					hotdog = new Scanner(new File("abc.txt"));
				} catch (FileNotFoundException e) {
				}
				while (hotdog.hasNext()) {
					data.add(Double.parseDouble(hotdog.next()));
				}
				hotdog.close();
				int n = 0;
				int size = data.size();
				while (true) {
					abc.handleValue(data.get(n));
					n = (n + 1) % size;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

}
