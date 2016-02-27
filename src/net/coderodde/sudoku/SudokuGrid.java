package net.coderodde.sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * This class implements a visual component representing a sudoku board.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 26, 2016)
 */
final class SudokuGrid extends JPanel {

    private static final Font FONT = new Font("Verdana", 
                                              Font.CENTER_BASELINE, 
                                              20);

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

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        Dimension fieldDimension = new Dimension(30, 30);

        class PopupMenuListener implements ActionListener {

            private final JTextField field;
            private final int number;
            
            PopupMenuListener(JTextField field, int number) {
                this.field  = field;
                this.number = number; 
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                field.setText("" + number);
            }
        }
        
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                JTextField field = grid[y][x];
                field.setBorder(border);
                field.setFont(FONT);
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setPreferredSize(fieldDimension);
                
                JPopupMenu menu = new JPopupMenu();
                
                for (int i = 0; i <= dimension; ++i) {
                    JMenuItem item = new JMenuItem("" + i);
                    menu.add(item);
                    item.addActionListener(new PopupMenuListener(field, i));
                }
                
                field.add(menu);
                field.setComponentPopupMenu(menu);
            }
        }

        int minisquareDimension = (int) Math.sqrt(dimension);
        this.gridPanel.setLayout(new GridLayout(minisquareDimension,
                                                minisquareDimension));

        this.minisquarePanels = new JPanel[minisquareDimension]
                                          [minisquareDimension];

        Border minisquareBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

        for (int y = 0; y < minisquareDimension; ++y) {
            for (int x = 0; x < minisquareDimension; ++x) {
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(minisquareDimension,
                                               minisquareDimension));
                panel.setBorder(minisquareBorder);
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

        clearButton.addActionListener((ActionEvent e) -> {
            clearAll();
        });

        solveButton.addActionListener((ActionEvent e) -> {
            solve();
        });
    }

    int getDimension() {
        return dimension;
    }
    
    void openFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            clearAll();

            int cells = dimension * dimension;
            int y = 0;
            int x = 0;

            while (cells > 0) {
                if (scanner.hasNext()) {
                    String text = scanner.next();

                    try {
                        int number = Integer.parseInt(text);

                        if (number > 0 && number <= dimension) {
                            grid[y][x].setText(" " + number);
                        }
                    } catch (NumberFormatException ex) {

                    }

                    ++x;

                    if (x == dimension) {
                        x = 0;
                        ++y;
                    }
                } else {
                    break;
                }

                --cells;
            }
        } catch (FileNotFoundException ex) {

        }
    }

    private void addSpace(JTextField field) {
        if (field.getText().isEmpty()) {
            field.setText(" ");
        }
    }

    void moveCursor(JTextField field, char c) {
        Point coordinates = mapFieldToCoordinates.get(field);
        field.setBackground(Color.WHITE);

        switch (c) {
            case 'w':
            case 'W':

                if (coordinates.y > 0) {
                    grid[coordinates.y - 1][coordinates.x].requestFocus();
                    addSpace(grid[coordinates.y - 1][coordinates.x]);
                }

                break;

            case 'd':
            case 'D':

                if (coordinates.x < dimension - 1) {
                    grid[coordinates.y][coordinates.x + 1].requestFocus();
                    addSpace(grid[coordinates.y][coordinates.x + 1]);
                }

                break;

            case 's':
            case 'S':

                if (coordinates.y < dimension - 1) {
                    grid[coordinates.y + 1][coordinates.x].requestFocus();
                    addSpace(grid[coordinates.y + 1][coordinates.x]);
                }

                break;

            case 'a':
            case 'A':

                if (coordinates.x > 0) {
                    grid[coordinates.y][coordinates.x - 1].requestFocus();
                    addSpace(grid[coordinates.y][coordinates.x - 1]);
                }

                break;
        }
    }

    void clearAll() {
        for (JTextField[] row : grid) {
            for (JTextField field : row) {
                field.setText("");
            }
        }
    }

    void solve() {
        Sudoku sudoku = new Sudoku(dimension);

        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                String text = grid[y][x].getText();

                int number = -1;

                try {
                    number = Integer.parseInt(text.trim());
                } catch (NumberFormatException ex) {

                }

                sudoku.set(x, y, number);
            }
        }

        try {
            Sudoku solution = new SudokuSolver(dimension).solve(sudoku);
            String skip = dimension < 10 ? " " : "";

            for (int y = 0; y < dimension; ++y) {
                for (int x = 0; x < dimension; ++x) {
                    grid[y][x].setText(skip + solution.get(x, y));
                }
            }  

            if (!solution.isValid()) {
                throw new RuntimeException("Something gone wrong.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                                          ex.getMessage(),
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
}
