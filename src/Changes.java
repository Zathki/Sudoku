
public class Changes {
	Square square;
	boolean isOption = false;
	int value = 0;
	boolean prevValue = false;
	
	public Changes(Square square) {
		this.square = square;
	}
	
	public Changes(Square square, boolean isOption, int value, boolean prevValue) {
		this.square = square;
		this.isOption = isOption;
		this.value = value;
		this.prevValue = prevValue;
	}
}
