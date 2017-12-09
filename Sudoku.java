package sudoku;

import java.util.ArrayList;

public class Sudoku {
	private static final int[][] EMPTY_PUZZLE = {	{0,0,0,0,0,0,0,0,0},
													{0,0,0,0,0,0,0,0,0},
													{0,0,0,0,0,0,0,0,0},
													{0,0,0,0,0,0,0,0,0},
													{0,0,0,0,0,0,0,0,0},
													{0,0,0,0,0,0,0,0,0},
													{0,0,0,0,0,0,0,0,0},
													{0,0,0,0,0,0,0,0,0},
													{0,0,0,0,0,0,0,0,0}};
	
	public static ArrayList<int[][]> solve(int[][] puzzle) {
		Solve solver = new Solve(puzzle);
		//solver.setMaxNumSolutions(500);
		solver.reset();
		return solver.getSolutions();
	}

	public static ArrayList<int[][]> solve(int[][] puzzle, int maxNumSolutions) {
		Solve solver = new Solve(puzzle);
		solver.setMaxNumSolutions(maxNumSolutions);
		solver.reset();
		return solver.getSolutions();
	}
	
	
	public static boolean onlyOneSolution(int[][] puzzle) {
		Solve solver = new Solve(puzzle);
		solver.reset();
		return solver.oneSolutionExists();		
	}
	
	public static int[][] getRandomPuzzle() {
		return solve(EMPTY_PUZZLE,1).get(0);
	}
}