package net.coderodde.sudoku;

import javax.swing.JFrame;

/**
 *
 * @author Rodion "rodde" Efremov
 */
public class SudokuFrame {
    
    private final JFrame frame = new JFrame("Sudoku solver");
    
    public SudokuFrame() {
        frame.getContentPane().add(new SudokuGrid(9));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
