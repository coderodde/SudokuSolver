package net.coderodde.sudoku;

/**
 * This class represents a sudoku matrix. It may be of any dimension and may 
 * contain arbitrary values at each cells.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 25, 2016)
 */
public class Sudoku {

    public static final int MINIMUM_DIMENSION = 1;
    private final int[][] matrix;

    public Sudoku(int dimension) {
        checkDimension(dimension);
        this.matrix = new int[dimension][dimension];
    }

    public Sudoku(Sudoku sudoku) {
        this(sudoku.matrix.length);

        for (int y = 0; y < sudoku.matrix.length; ++y) {
            this.matrix[y] = sudoku.matrix[y].clone();
        }
    }

    public int get(int x, int y) {
        return matrix[y][x];
    }

    public void set(int x, int y, int value) {
        matrix[y][x] = value;
    }

    public int getDimension() {
        return matrix.length;
    }

    public boolean isValid() {
        int dimension = getDimension();
        int minisquareDimension = getMinisquareDimension();
        IntSet[] rowSetArray    = new IntSet[dimension];
        IntSet[] columnSetArray = new IntSet[dimension];
        IntSet[][] minisquareSetMatrix = new IntSet[minisquareDimension]
                                                   [minisquareDimension];

        for (int i = 0; i < dimension; ++i) {
            rowSetArray[i]    = new IntSet(dimension + 1);
            columnSetArray[i] = new IntSet(dimension + 1);
        }

        for (int squareY = 0; squareY < minisquareDimension; ++squareY) {
            for (int squareX = 0; squareX < minisquareDimension; ++squareX) {
                minisquareSetMatrix[squareY]
                                   [squareX] = new IntSet(dimension + 1);
            }
        }

        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                int currentValue = get(x, y);

                if (rowSetArray[y].contains(currentValue)
                        || columnSetArray[x].contains(currentValue)) {
                    return false;
                }

                int squareX = x / minisquareDimension;
                int squareY = y / minisquareDimension;

                if (minisquareSetMatrix[squareY]
                                       [squareX].contains(currentValue)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String maximumCellValueString = "" + matrix.length;
        int fieldLength = maximumCellValueString.length();
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < matrix.length; ++y) {
            sb.append(rowToString(y, fieldLength));
            sb.append('\n');
        }

        return sb.toString();
    }

    private String rowToString(int y, int fieldLength) {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < matrix.length; ++x) {
            sb.append(String.format("%" + fieldLength + "d", get(x, y)));

            if (x < matrix.length - 1) {
                sb.append(' ');
            }
        }

        return sb.toString();
    } 

    private void checkDimension(int dimension) {
        if (dimension < MINIMUM_DIMENSION) {
            throw new IllegalArgumentException(
                    "The input dimension is too low: " + dimension + 
                    ", should be at least " + MINIMUM_DIMENSION + ".");
        } 
    }

    private int getMinisquareDimension() {
        int i = 1;
        int dimension = getDimension();

        while (i * i < dimension) {
            ++i;
        }

        if (i * i > dimension) {
            throw new IllegalStateException(
                    "Impossible to form minisquares: " +
                    "the dimension of this sudoku (" + dimension + ") is not " +
                    "a square of a positive integer");
        }

        return i;
    }
}
