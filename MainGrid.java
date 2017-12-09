package sudoku;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//This class holds all of the information for cells and has operations to performed on cells, however never actually changes
//cell information without explicit instruction from a method call outside of the class.
class MainGrid {
	private final Cell CELLBLANK = new Cell();
	private Cell[][] grid = new Cell[9][9];
	private Box[][] boxes = new Box[3][3];
	private Row[] rows = new Row[9];
	private Column[] cols = new Column[9];

	protected MainGrid(int[][] puzzle) {
		for (int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if (puzzle[i][j] != 0)
					grid[i][j] = new Cell(i,j,puzzle[i][j]);
				else
					grid[i][j] = new Cell(i,j);
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				boxes[i][j] = new Box(i,j);
			}
		}
		for (int i = 0; i < 9; i++) {
			rows[i] = new Row(i);
			cols[i] = new Column(i);
		}
	}

	//This method calls updateFound on all boxes, rows, and columns, which updates information within boxes, rows, and columns,
	//then removes possibilities for cell values through the update found methods.
	protected void removePoss() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				boxes[i][j].updateFound();
			}
		}
		for (int i = 0; i < 9; i++) {
			rows[i].updateFound();
			cols[i].updateFound();
		}
	}

	//Returns grid
	protected int[][] getGrid() {
		int[][] returnGrid = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				returnGrid[i][j] = grid[i][j].getValue();
			}
		}
		return returnGrid;
	}

	//Finds a cell in an unsolved state. This is the cell furthest to the top of the grid then furthest to the left.
	protected Cell findUnsolvedCell() {
		for (int i = 0; i < 9 ; i++) {
			for (int j = 0; j < 9; j++) {
				if(!grid[i][j].isFound())
					return grid[i][j];
			}
		}
		return CELLBLANK;
	}

	//This method invokes the findIn methods for box, row, and col and findByPoss and then returns if a value was found.
	protected boolean findAll() {
		boolean someChange = false;
		someChange |= findInBoxes();
		someChange |= findInRows();
		someChange |= findInCols();
		someChange |= findByOnlyPoss();
		return someChange;
	}

	//Searches each box and each value within box to see if there is a cell with value that can be found using box.onlyPossInBox(value)
	private boolean findInBoxes() {
		boolean someChange = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int value = 1; value < 10; value++) {
					Optional<Cell> potentialCell = boxes[i][j].onlyPossInBox(value);
					if (potentialCell.isPresent()) {
						Cell found = potentialCell.get();
						found.setValue(value);
						someChange = true;
						removePoss();
					}
				}
			}
		}
		return someChange;
	}

	//Searches each box and each value within rows to see if there is a cell with value that can be found using row.onlyPossInBox(value)
	private boolean findInRows() {
		boolean someChange = false;
		for (int i = 0; i < 9; i++) {
			for (int value = 1; value < 10; value++) {
				Optional<Cell> potentialCell = rows[i].onlyPossInRow(value);
				if (potentialCell.isPresent()) {
					Cell found = potentialCell.get();
					found.setValue(value);
					someChange = true;
					removePoss();
				}
			}
		}
		return someChange;
	}

	//Searches each box and each value within cols to see if there is a cell with value that can be found using col.onlyPossInBox(value)
	private boolean findInCols() {
		boolean someChange = false;
		for (int i = 0; i < 9; i++) {
			for (int value = 1; value < 10; value++) {
				Optional<Cell> potentialCell = cols[i].onlyPossInCol(value);
				if (potentialCell.isPresent()) {
					Cell found = potentialCell.get();
					found.setValue(value);
					someChange = true;
					removePoss();
				}
			}
		}
		return someChange;
	}	

	//This method goes through each cell and check to see if the cell has only one possibility. If only one possibility exists
	//then the value for the cell is set and all update is called upon the cell. If there is some change to some cell
	//then someChange is changed to true and true is returned, otherwise false.
	private boolean findByOnlyPoss() {
		boolean someChange = false;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!grid[i][j].isFound()) {
					int possValue = 0;
					int numTrue = 0;
					for (int value = 1; value < 10; value++) {
						if (grid[i][j].getPoss(value)) {
							possValue = value;
							numTrue++;
						}
						if (numTrue > 1)
							break;
					}
					if (numTrue == 1) {
						grid[i][j].setValue(possValue);
						someChange = true;
						removePoss();
					}
				}
			}
		}
		return someChange;
	}

	//This method goes through each cell and checks to see if the value has been found or if there are possibilities for value.
	//If there are no possibilities and the value has not been found then there is no solution to the puzzle.
	protected boolean checkPossibleSolution() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boolean hasPoss = false;
				if (grid[i][j].isFound())
					hasPoss = true;
				else
					for (int value = 1; value < 10; value++) {
						if (grid[i][j].getPoss(value)) {
							hasPoss = true;
							break;
						}
					}
				if (!hasPoss)
					return false;
			}
		}
		return true;
	}

	protected boolean checkValidPuzzle() {
		removePoss();
		boolean valid = true;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				valid &= boxes[i][j].validBox();
			}
		}
		for (int i = 0; i < 9; i++) {
			valid &= rows[i].validRow();
			valid &= cols[i].validCol();
		}
		return valid;
	}

	//This method checks every cell and checks to see if a value has been found for the cell. If a value has not been found
	//false is returned, otherwise true
	protected boolean checkForSolution() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				if (!grid[i][j].isFound())
					return false;
			}
		}
		return true;
	}

	class Box {
		private Cell[][] elements = new Cell[3][3];
		private Map<Integer,Boolean> found = new HashMap<Integer,Boolean>();

		protected Box(int boxRow,int boxCol) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					elements[i][j] = grid[boxRow*3+i][boxCol*3+j];

				}
			}
			for (int i = 1; i < 10; i++) {
				found.put(i, false);				
			}
		}

		//Updates in found that value has been found within box, calls removePoss with found value
		protected void updateFound() {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if(elements[i][j].isFound() && !found.get(elements[i][j].getValue())) {
						found.put(elements[i][j].getValue(), true);
						removePoss(elements[i][j].getValue());
					}
				}
			}
		}

		//Removes a passed in value as a possibility from every element within box
		protected void removePoss(int value) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					elements[i][j].setPossFalse(value);
				}
			}
		}

		//Looks at all cells within box that possess a value, if only one possesses the value then that cell must have that value
		//Returns Optional<Cell> if cell if found, otherwise returns Optional.empty()
		protected Optional<Cell> onlyPossInBox(int value) {
			int numFound = 0;
			Cell foundPoss = CELLBLANK;
			Optional<Cell> found = Optional.empty();
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (elements[i][j].getPoss(value)) {
						foundPoss = elements[i][j];
						numFound++;
					}
				}
			}
			if (numFound == 1)
				found = Optional.of(foundPoss);
			return found;
		}

		//If a duplicate value is discovered false is returned, otherwise true.
		protected boolean validBox() {
			for (int value = 1; value < 10; value++) {
				if(found.get(value)) {
					int instances = 0;
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							if(elements[i][j].isFound() && elements[i][j].getValue() == value) {
								instances++;
								if(instances > 1)
									return false;
							}
						}
					}
				}
			}
			return true;
		}
	}

	class Column {
		private Cell[] elements = new Cell[9];
		private Map<Integer,Boolean> found = new HashMap<Integer,Boolean>();

		protected Column(int rowNum) {
			for (int i = 0; i < 9; i++) {
				elements[i] = grid[rowNum][i];
			}
			for (int i = 1; i < 10; i++) {
				found.put(i, false);				
			}
		}
		//Updates in found that value has been found within column, calls removePoss with found value
		protected void updateFound() {
			for (int i = 0; i < 9; i++) {
				if(elements[i].isFound() && !found.get(elements[i].getValue())) {
					found.put(elements[i].getValue(), true);
					removePoss(elements[i].getValue());
				}
			}
		}

		//Removes a passed in value as a possibility from every element within column
		protected void removePoss(int value) {
			for (int i = 0; i < 9; i++) {
				elements[i].setPossFalse(value);
			}
		}

		//Looks at all cells within col that possess a value, if only one possesses the value then that cell must have that value
		//Returns Optional<Cell> if cell if found, otherwise returns Optional.empty()
		protected Optional<Cell> onlyPossInCol(int value) {
			int numFound = 0;
			Cell foundPoss = CELLBLANK;
			Optional<Cell> found = Optional.empty();
			for (int i = 0; i < 9; i++) {
				if (elements[i].getPoss(value)) {
					foundPoss = elements[i];
					numFound++;
				}
			}
			if (numFound == 1)
				found = Optional.of(foundPoss);
			return found;
		}

		//If a duplicate value is discovered false is returned, otherwise true.
		protected boolean validCol() {
			for (int value = 1; value < 10; value++) {
				if(found.get(value)) {
					int instances = 0;
					for (int i = 0; i < 9; i++) {
						if(elements[i].isFound() && elements[i].getValue() == value) {
							instances++;
							if(instances > 1)
								return false;
						}
					}
				}
			}
			return true;
		}
	}	

	class Row {
		private Cell[] elements = new Cell[9];
		private Map<Integer,Boolean> found = new HashMap<Integer,Boolean>();

		protected Row(int colNum) {
			for (int i = 0; i < 9; i++) {
				elements[i] = grid[i][colNum];
			}
			for (int i = 1; i < 10; i++) {
				found.put(i, false);				
			}
		}
		//Updates in found that value has been found within row, calls removePoss with found value
		protected void updateFound() {
			for (int i = 0; i < 9; i++) {
				if(elements[i].isFound() && !found.get(elements[i].getValue())) {
					found.put(elements[i].getValue(), true);
					removePoss(elements[i].getValue());
				}
			}
		}

		//Removes a passed in value as a possibility from every element within row
		protected void removePoss(int value) {
			for (int i = 0; i < 9; i++) {
				elements[i].setPossFalse(value);
			}
		}

		//Looks at all cells within row that possess a value, if only one possesses the value then that cell must have that value
		//Returns Optional<Cell> if cell if found, otherwise returns Optional.empty()
		protected Optional<Cell> onlyPossInRow(int value) {
			int numFound = 0;
			Cell foundPoss = CELLBLANK;
			Optional<Cell> found = Optional.empty();
			for (int i = 0; i < 9; i++) {
				if (elements[i].getPoss(value)) {
					foundPoss = elements[i];
					numFound++;
				}
			}
			if (numFound == 1)
				found = Optional.of(foundPoss);
			return found;
		}
		
		//If a duplicate value is discovered false is returned, otherwise true.
		protected boolean validRow() {
			for (int value = 1; value < 10; value++) {
				if(found.get(value)) {
					int instances = 0;
					for (int i = 0; i < 9; i++) {
						if(elements[i].isFound() && elements[i].getValue() == value) {
							instances++;
							if(instances > 1)
								return false;
						}
					}
				}
			}
			return true;
		}
	}
}