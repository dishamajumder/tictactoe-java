import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class TicTacToeGUI extends JFrame {

    private final char PLAYER_X = 'X';
    private final char PLAYER_O = 'O';
    private char currentPlayer;
    private char[][] board;
    private JButton[][] buttons;
    private JLabel statusLabel;

    public TicTacToeGUI() {
        // Initialize the game board and state
        board = new char[3][3];
        buttons = new JButton[3][3];
        currentPlayer = PLAYER_X; // Player X starts

        // --- Set up the main window (JFrame) ---
        setTitle("Tic-Tac-Toe");
        setSize(400, 450); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Create the game board panel ---
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));

        // Create and add buttons to the board panel
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }
        
        // --- Create the status label and reset button ---
        statusLabel = new JLabel("Player X's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> initializeGame());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(resetButton, BorderLayout.SOUTH);

        // Add components to the main window
        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize the game for the first time
        initializeGame();
        setVisible(true); // Make the window visible
    }

    // Initialize or reset the game state
    private void initializeGame() {
        currentPlayer = PLAYER_X;
        statusLabel.setText("Player X's turn");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true); // Re-enable all buttons
            }
        }
    }

    // Handle button clicks for player moves
    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // This is the input validation: only allow moves on empty cells
            if (board[row][col] == '-' && currentPlayer == PLAYER_X) {
                makeMove(row, col);

                // Check game status after player's move
                if (!checkWinner() && !isBoardFull()) {
                    // If the game is not over, it's AI's turn
                    aiMove();
                }
            }
        }
    }
    
    // Logic for making a move
    private void makeMove(int row, int col) {
        board[row][col] = currentPlayer;
        buttons[row][col].setText(String.valueOf(currentPlayer));
        buttons[row][col].setEnabled(false); // Disable button after move

        if (checkWinner()) {
            endGame(currentPlayer + " wins!");
        } else if (isBoardFull()) {
            endGame("It's a draw!");
        } else {
            // Switch player
            currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
            statusLabel.setText("Player " + currentPlayer + "'s turn");
        }
    }

    // AI Opponent Logic: Makes a random valid move
    private void aiMove() {
        // Find all available (empty) cells
        ArrayList<Point> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    emptyCells.add(new Point(i, j));
                }
            }
        }

        // Pick a random empty cell
        if (!emptyCells.isEmpty()) {
            Random rand = new Random();
            Point randomMove = emptyCells.get(rand.nextInt(emptyCells.size()));
            int row = randomMove.x;
            int col = randomMove.y;
            
            // Short delay to simulate "thinking"
            Timer timer = new Timer(500, e -> makeMove(row, col));
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    // Check for a winner
    private boolean checkWinner() {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) ||
                (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer)) {
                return true;
            }
        }
        // Check diagonals
        return (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
               (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer);
    }

    // Check if the board is full
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    // End the game
    private void endGame(String message) {
        statusLabel.setText(message);
        // Disable all buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(this, message + "\nClick 'Reset Game' to play again.");
    }

    // Main method to run the application
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> new TicTacToeGUI());
    }
}