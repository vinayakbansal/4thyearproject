/**
 * 
 * @author Vinayak Bansal Dated Apr 3, 2014 
 */

import java.util.Random;

public class MockArduino {

	public MockArduino(final IHandleData abc) {
		new Thread() {
			public void run() {
//				List<Double> data = new ArrayList<Double>();
//				Scanner hotdog = null;
//				try {
//					hotdog = new Scanner(new File("src/Files/abc.txt"));
//				} catch (FileNotFoundException e) {
//					System.out.println("fdasf");
//				}
//				while (hotdog.hasNext()) {
//					data.add(Double.parseDouble(hotdog.next()));
//				}
//				hotdog.close();
//				int n = 0;
//				int size = data.size();
				Random random = new Random();
				while (true) {
					abc.handleValue(random.nextInt(1024));
//					n = (n + 1) % size;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

}
