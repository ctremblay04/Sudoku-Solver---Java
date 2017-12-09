package sudoku;

import java.util.HashMap;
import java.util.Map;

class Cell {
	private Map<Integer,Boolean> poss = new HashMap<Integer,Boolean>();
	private boolean found = false;
	private int value;
	private int row;
	private int col;
	
	public Cell() {
		for (int i = 0; i < 9; i++) {
			poss.put(i+1, true);
		}
	}
	
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
		for (int i = 0; i < 9; i++) {
			poss.put(i+1, true);
		}
	}
	
	public Cell(int row, int col, int value) {
		this.value = value;
		this.found = true;
		for (int i = 1; i < 10; i++) {
			if (i != value)
				poss.put(i, false);
			else
				poss.put(i, true);
		}
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean getPoss(int value) {
		return poss.get(value);
	}

	public void setPossFalse(int value) {
		poss.put(value,false);
	}

	public boolean isFound() {
		return found;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		this.found = true;
	}
	
	
}