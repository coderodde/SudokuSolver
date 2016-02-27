package net.coderodde.sudoku;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 * This class implements a key listener for individual sudoku board cells.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 26, 2016)
 */
class SudokuCellKeyListener implements KeyListener {

    private final SudokuGrid grid;

    SudokuCellKeyListener(SudokuGrid grid) {
        this.grid = grid;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        JTextField textField = (JTextField) e.getSource();
        
        switch (c) {
            case 'a':
            case 'A':
            case 's':
            case 'S':
            case 'd':
            case 'D':
            case 'w':
            case 'W':
                e.consume();
                grid.moveCursor(textField, c);
        }
        
        String s = "" + grid.getDimension();
        int digits = s.length();
        
        if (textField.getText().length() >= digits) {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
