package sudoku;

import java.util.ArrayList;

public class Solve {
	private MainGrid puzzle;
	private ArrayList<int[][]> solutions = new ArrayList<int[][]>();
	private static int numSolutions = 0;
	private static int maxNumSolutions = 1;
	
	//This is a class which I will possibly use to separate the mainGrid from the functions which I use to solve the grid.
	//Seems like a more natural way to do things.
	public Solve(int[][] puzzle) {
		this.puzzle = new MainGrid(puzzle);
		this.puzzle.removePoss();
	}
	
	public void setMaxNumSolutions(int maxNum) {
		maxNumSolutions = maxNum;
	}
	
	//Main method which sequences through methods that solve puzzle, exits if puzzle has been solved or if no solution can be found or
	//if solveByTrialAndError is called.
	private void solvePuzzle() {
		if (puzzle.checkValidPuzzle()) {
			boolean solutionFound = false;
			boolean possibleSolution = true;
			while (!solutionFound && possibleSolution) {
				
				puzzle.removePoss();
				
				boolean someChange = puzzle.findAll();
				
				solutionFound = puzzle.checkForSolution();
				if (!solutionFound)
					possibleSolution = puzzle.checkPossibleSolution();
				else if (numSolutions < maxNumSolutions) {	
					solutions.add(puzzle.getGrid());
					numSolutions++;
				}
				
				if (!someChange && numSolutions < maxNumSolutions) {
					solveByTrialAndError();
					break;
				}
				if (numSolutions == maxNumSolutions)
					break;
			}
		}
	}
	
	//Finds an unsolved cell and tries every possibility within that cell to find solutions by creating new MainGrid
	//instance with the possibility as the value for the unsolved cell. This is recursive.	
	private void solveByTrialAndError() {
		Cell testCell = puzzle.findUnsolvedCell();
		int testRow = testCell.getRow();
		int testCol = testCell.getCol();
		int[] values = randomizedValues();
		//for (int value = 1; value < 10; value++) {
		for (int value : values) {
			int[][] currentGrid = puzzle.getGrid();
			if(testCell.getPoss(value)) {
				Solve testGrid;
				currentGrid[testRow][testCol] = value;
				testGrid = new Solve(currentGrid);
				solutions.addAll(testGrid.getSolutions());
			}
		}
	}
	
	protected void reset() {
		numSolutions = 0;
	}
	
	protected ArrayList<int[][]> getSolutions() {
		solutions = new ArrayList<int[][]>();
		solvePuzzle();
		return solutions;
	}
	
	protected boolean oneSolutionExists() {
		ArrayList<int[][]> solutions = getSolutions();
		System.out.println(solutions.isEmpty());
		System.out.println(solutions.size());
		return (!solutions.isEmpty() && solutions.size() == 1);
	}
	
	private int[] randomizedValues() {
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int value = 1; value < 10; value++) {
			values.add(value);
		}
		int[] mixedValues = new int[9];
		for (int i = 0; i < 9; i++) {
			int index = (int)(Math.random()*(9-i));
			mixedValues[i] = values.get(index);
			values.remove(index);
		}
		return (mixedValues);
	}
}
