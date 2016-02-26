package net.coderodde.sudoku;

/**
 * This class implements a trivial integer set used for keeping track which 
 * cell values are allowed at each sudoku cell.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 25, 2016)
 */
final class IntSet {

    private final boolean[] table;

    IntSet(int sudokuDimension) {
        this.table = new boolean[sudokuDimension];
    }

    void add(int number) {
        table[number] = true;
    }

    boolean contains(int number) {
        return table[number];
    }

    void remove(int number) {
        table[number] = false;
    }

    void clear() {
        for (int i = 0; i < table.length; ++i) {
            table[i] = false;
        }
    }
}
