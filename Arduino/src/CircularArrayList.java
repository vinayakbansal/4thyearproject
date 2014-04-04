import java.util.*;

/**
 * If you use this code, please retain this comment block.
 * 
 * @author Isak du Preez isak at du-preez dot com www.du-preez.com
 */
public class CircularArrayList<E> extends AbstractList<E> {

	private int n; // buffer length
	private final List<E> buf; // a List implementing RandomAccess
	private int size;
	private final int capacity;

	public CircularArrayList(int capacity) {
		this.capacity = capacity;
		buf = new ArrayList<E>(Collections.nCopies(capacity, (E) null));
		n = 0;
		size = n;
	}

	public int capacity() {
		return n - 1;
	}

	@Override
	public int size() {
		return Math.min(size, capacity);
	}

	@Override
	public E get(int i) {
		if (i < 0 || i >= size()) {
			throw new IndexOutOfBoundsException();
		}
		return buf.get((i + n - 1) % capacity);
	}
	

	@Override
	public boolean add(E e) {
		buf.set(n, e);
		n++;
		size++;
		n %= capacity;
		return true;
	}

}