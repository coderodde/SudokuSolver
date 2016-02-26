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
                grid.moveCursor((JTextField) e.getSource(), c);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getExtendedKeyCode();
        
        if (keyCode == KeyEvent.VK_UP
                || keyCode == KeyEvent.VK_RIGHT
                || keyCode == KeyEvent.VK_DOWN
                || keyCode == KeyEvent.VK_LEFT) {
            return;
        }
        
        JTextField field = (JTextField) e.getSource();
        
        if (!field.getText().isEmpty() 
                && !Character.isSpaceChar(field.getText().charAt(0))) {
            field.setText(" " + field.getText());
        }
    }
}
