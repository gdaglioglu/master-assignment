package example.ui.models;

public class ModelSimple {

	private int x;

	public ModelSimple() {
		x = 0;
	}

	public ModelSimple(int x) {
		this.x = x;
	}

	public void incX() {
		x++;
	}

	public int getX() {
		return x;
	}
}
