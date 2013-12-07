package example.ui;

public class SimpleModel {

	private int x;

	public SimpleModel() {
		x = 0;
	}

	public SimpleModel(int x) {
		this.x = x;
	}

	public void incX() {
		x++;
	}

	public int getX() {
		return x;
	}
}
