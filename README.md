# Sudoku-Solver---Java
Java implementation of sudoku solver
The way that this project works is by performing quick efficent eliminations of possibilities in a puzzle, then when it gets stuck 
creates a new instance of a puzzle and attempts to solve it in the same fashion. This essentially performs a breadth first search 
on different possible sudoku configurations. It allows for all valid solutions to any puzzle to be generated as well, rather than 
searching for a single solution.
