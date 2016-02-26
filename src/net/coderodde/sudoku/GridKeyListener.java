package net.coderodde.sudoku;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author rodionefremov
 */
final class GridKeyListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_UP:
                System.out.println("up");
                break;
                
            case KeyEvent.VK_RIGHT:
                System.out.println("right");
                break;
                
            case KeyEvent.VK_DOWN:
                System.out.println("down");
                break;
                
            case KeyEvent.VK_LEFT:
                System.out.println("left");
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    
    }
}
