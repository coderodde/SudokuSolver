package net.coderodde.sudoku;

import java.util.Scanner;

/**
 * This class implements a command line utility for solving sudokus.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 25, 2016)
 */
public class App {

    /**
     * Default dimension of the input sudoku.
     */
    private static final int DEFAULT_DIMENSION = 9;

    private final int dimension;
    private Sudoku inputSudoku;
    private SudokuSolver solver;

    public App(int dimension) {
        this.dimension = dimension;

        try {
            this.inputSudoku = new Sudoku(dimension);
        } catch (IllegalArgumentException ex) {
            System.err.println("ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }

    private void createSolver() {
        this.solver = new SudokuSolver(dimension);
    }

    private void scanDigits() {
        int cellsToScan = dimension * dimension;
        int y = 0;
        int x = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (cellsToScan > 0) {
                if (!scanner.hasNextInt()) {
                    break;
                }

                int cellValue = scanner.nextInt();
                inputSudoku.set(x++, y, cellValue);

                if (x == dimension) {
                    x = 0;
                    ++y;
                }

                --cellsToScan;
            }
        }
    }

    private void solve() {
        System.out.println("[Input sudoku]");
        System.out.println(inputSudoku);

        try {
            long startTime = System.nanoTime();
            Sudoku solution = solver.solve(inputSudoku);
            long endTime = System.nanoTime();

            if (!solution.isValid()) {
                throw new IllegalStateException(
                        "ERROR: The computed solution is not valid.");
            }

            System.out.println("[Solution]");
            System.out.println(solution);
            System.out.printf("Solution took %.2f milliseconds.\n", 
                              (endTime - startTime) / 1e6);
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            int requestedDimension = getDimension(args);
            App app = new App(requestedDimension);

            try {
                app.createSolver();
            } catch (IllegalArgumentException ex) {
                System.err.println("ERROR: " + ex.getMessage());
                System.exit(1);
            }

            app.scanDigits();
            app.solve();
        } else {
            javax.swing.SwingUtilities.invokeLater(() -> { 
                new SudokuFrame(); 
            });
        }
    }

    private static int getDimension(String[] args) {
        int dimension = DEFAULT_DIMENSION;

        if (args.length > 0) {
            try {
                dimension = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                System.err.println(
                        "ERROR: \"" + args[0] + "\" is not an integer.");
                System.exit(1);
            }
        }

        return dimension;
    }
}
