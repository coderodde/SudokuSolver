package net.coderodde.sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * This class implements a visual component representing a sudoku board.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 26, 2016)
 */
final class SudokuGrid extends JPanel {
    
    private static final Font FONT = new Font("Verdana", Font.CENTER_BASELINE, 20);
    
    private final JTextField[][] grid;
    private final Map<JTextField, Point> mapFieldToCoordinates = 
            new HashMap<>();
    
    private final int dimension;
    private final JPanel gridPanel;
    private final JPanel buttonPanel;
    private final JButton solveButton;
    private final JButton clearButton;
    private final JPanel[][] minisquarePanels;
    
    SudokuGrid(int dimension) {
        this.grid = new JTextField[dimension][dimension];
        this.dimension = dimension;
        
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                JTextField field = new JTextField();
                field.addKeyListener(new SudokuCellKeyListener(this));
                mapFieldToCoordinates.put(field, new Point(x, y));
                grid[y][x] = field;
            }
        }
        
        this.gridPanel   = new JPanel();
        this.buttonPanel = new JPanel();
      
        this.addKeyListener(new GridKeyListener());
        
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        Dimension fieldDimension = new Dimension(30, 30);
        
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                JTextField field = grid[y][x];
                field.setBorder(border);
                field.setFont(FONT);
                field.setPreferredSize(fieldDimension);
            }
        }
       
        int minisquareDimension = (int) Math.sqrt(dimension);
        this.gridPanel.setLayout(new GridLayout(minisquareDimension,
                                                minisquareDimension));
        
        this.minisquarePanels = new JPanel[minisquareDimension]
                                          [minisquareDimension];
        
        for (int y = 0; y < minisquareDimension; ++y) {
            for (int x = 0; x < minisquareDimension; ++x) {
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(minisquareDimension,
                                               minisquareDimension,
                                               0,
                                               0));
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                minisquarePanels[y][x] = panel;
                gridPanel.add(panel);
            }
        }
        
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                int minisquareX = x / minisquareDimension;
                int minisquareY = y / minisquareDimension;
                
                minisquarePanels[minisquareY][minisquareX].add(grid[y][x]);
            }
        }
        
        this.gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 
                                                                2));
        this.clearButton = new JButton("Clear");
        this.solveButton = new JButton("Solve");
        
        this.buttonPanel.setLayout(new BorderLayout());
        this.buttonPanel.add(clearButton, BorderLayout.WEST);
        this.buttonPanel.add(solveButton, BorderLayout.EAST);
        
        this.setLayout(new BorderLayout());
        this.add(gridPanel,   BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);   
    }
    
    void moveCursor(JTextField field, int keyCode) {
        Point coordinates = mapFieldToCoordinates.get(field);
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
                
                break;
                
            case KeyEvent.VK_RIGHT:
                
                break;
                
            case KeyEvent.VK_DOWN:
                
                break;
                
            case KeyEvent.VK_LEFT:
                
                break;
        }
    }
}
