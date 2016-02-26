package net.coderodde.sudoku;

import java.awt.Point;
import java.util.Objects;

/**
 * This class implements a backtracking algorithm for solving square sudokus.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 25, 2016)
 */
public class SudokuSolver {

    private static final int MINIMUM_DIMENSION = 1;
    private static final int UNUSED = 0;

    private final int dimension;
    private final int minisquareDimension;
    private Sudoku input;
    private final IntSet[] rowSetArray;
    private final IntSet[] columnSetArray;
    private final IntSet[][] minisquareSetMatrix;
    private final Point point = new Point();
    private Sudoku solution;

    public SudokuSolver(int dimension) {
        this.minisquareDimension = checkDimension(dimension);
        this.dimension = dimension;

        rowSetArray    = new IntSet[dimension];
        columnSetArray = new IntSet[dimension];

        minisquareSetMatrix = new IntSet[minisquareDimension]
                                        [minisquareDimension];

        for (int i = 0; i < dimension; ++i) {
            rowSetArray   [i] = new IntSet(dimension + 1);
            columnSetArray[i] = new IntSet(dimension + 1);
        }

        for (int y = 0; y < minisquareDimension; ++y) {
            for (int x = 0; x < minisquareDimension; ++x) {
                minisquareSetMatrix[y][x] = new IntSet(dimension + 1);
            }
        }
    }

    public Sudoku solve(Sudoku input) {
        Objects.requireNonNull(input, "The input sudoku is null.");
        this.input = new Sudoku(input);
        fixInputSudoku();
        clearSets();
        tryInitializeSets();
        solution = new Sudoku(dimension);
        solve();
        return solution;
    }

    // Checks that the requested dimension d = k^2 for some positive integer 
    // k, which means that we can form "miniquares". For example, if the 
    // dimension is 9, the dimension of a minisquare is 3.
    private int checkDimension(int dimension) {
        if (dimension < MINIMUM_DIMENSION) {
            throw new IllegalArgumentException(
                    "The requested sudoku dimension (" + dimension +
                    ") is too small. Must be at least " + MINIMUM_DIMENSION + 
                    ".");
        }

        int i = 1;

        while (i * i < dimension) {
            ++i;
        }

        if (i * i > dimension) {
            throw new IllegalArgumentException(
                    "The requested sudoku dimension (" + dimension + 
                    ") is not a square of a positive integer.");
        }

        return i;
    }

    private void fixInputSudoku() {
        int dimension = input.getDimension();

        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                int currentValue = input.get(x, y);

                if (currentValue < 1 || currentValue > dimension) {
                    input.set(x, y, UNUSED);
                }
            }
        }
    }

    private void clearSets() {
        for (int i = 0; i < dimension; ++i) {
            rowSetArray   [i].clear();
            columnSetArray[i].clear();
        }

        for (int y = 0; y < minisquareDimension; ++y) {
            for (int x = 0; x < minisquareDimension; ++x) {
                minisquareSetMatrix[y][x].clear();
            }
        }
    }

    private void tryInitializeSets() {
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                int currentValue = input.get(x, y);

                if (rowSetArray[y].contains(currentValue)) {
                    throw new IllegalArgumentException(
                        "The cell (x = " + x + ", y = " + y + ") with " +
                        "value " + currentValue + 
                        " is a duplicate in its row.");
                }

                if (columnSetArray[x].contains(currentValue)) {
                    throw new IllegalArgumentException(
                        "The cell (x = " + x + ", y = " + y + ") with " +
                        "value " + currentValue + 
                        " is a duplicate in its column.");
                }

                loadMinisquareCoordinates(x, y);

                if (minisquareSetMatrix[point.y][point.x].contains(currentValue)) {
                    throw new IllegalArgumentException(
                        "The cell (x = " + x + ", y = " + y + ") with " +
                        "value " + currentValue + 
                        " is a duplicate in its minisquare.");
                }

                if (isValidCellValue(currentValue)) {
                    rowSetArray   [y].add(currentValue);
                    columnSetArray[x].add(currentValue);
                    // This call saves the result in the field 'point'.
                    minisquareSetMatrix[point.y][point.x].add(currentValue);
                }
            }
        }
    }

    private boolean isValidCellValue(int value) {
        return 0 < value && value <= dimension;
    }

    private void loadMinisquareCoordinates(int x, int y) {
        point.x = x / minisquareDimension;
        point.y = y / minisquareDimension;
    }

    private void solve() {
        solve(0, 0);
    }

    private boolean solve(int x, int y) {
        if (x == dimension) {
            // "Carriage return": we are done with row 'y', so move to the row
            // 'y + 1' and set 'x' to zero.
            x = 0;
            ++y;
        }

        if (y == dimension) {
            // We have found a solution, signal success by return 'true'.
            return true;
        }

        if (input.get(x, y) != UNUSED) {
            // Just load a predefined value from the input matrix to solution,
            // and proceed further.
            solution.set(x, y, input.get(x, y));
            return solve(x + 1, y);
        } 

            // Find least number fitting in the current cell (x, y).
        for (int i = 1; i <= dimension; ++i) {
            if (!columnSetArray[x].contains(i)
                    && !rowSetArray[y].contains(i)) {
                loadMinisquareCoordinates(x, y);

                if (!minisquareSetMatrix[point.y][point.x].contains(i)) {
                    solution.set(x, y, i);
                    rowSetArray   [y].add(i);
                    columnSetArray[x].add(i);
                    minisquareSetMatrix[point.y][point.x].add(i);

                    if (solve(x + 1, y)) {
                        // A solution found; stop backtracking by returning
                        // at each recursion level.
                        return true;
                    }

                    // Setting 'i' at current cell (x, y) did not lead towards
                    // solution; remove from the sets and try larger value 
                    // for 'i' in the next iteration.
                    rowSetArray   [y].remove(i);
                    columnSetArray[x].remove(i);

                    // Reload the minisquare coordinates as they are likely to
                    // be invalid due to recursion.
                    loadMinisquareCoordinates(x, y);
                    minisquareSetMatrix[point.y][point.x].remove(i);
                }
            }
        }

        // No number fits at this (x, y), backtrack a little.
        return false;
    }
}
