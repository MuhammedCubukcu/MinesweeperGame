import java.util.Random;
import java.util.Scanner;

public class MineSweeper {
    private int row;
    private int col;
    private String[][] mineMatrix;
    private String[][] playerMatrix;
    private boolean[][] revealed;

    // Constructor
    public MineSweeper() {
        getDimension();
        this.mineMatrix = createdMatrix();
        this.playerMatrix = createdMatrix();
        this.revealed = new boolean[this.row][this.col];
        fillMines();
        initializePlayerMatrix();
    }

    // Matrix Determine size
    public void getDimension() {
        Scanner inp = new Scanner(System.in);
        System.out.println("Determine size");
        System.out.println("Please do not enter the size smaller than 2x2, the minimum size is 2x2.");
        boolean isMatrix = true;
        do {
            System.out.print("Row: ");
            this.row = inp.nextInt();
            System.out.print("Column: ");
            this.col = inp.nextInt();
            if (this.row >= 2 && this.col >= 2) {
                isMatrix = false;
            } else {
                System.out.println("Invalid size. The minimum size is 2x2.");
            }
        } while (isMatrix);
        System.out.println("Successfully " + "Row: " + this.row + " Col: " + this.col);
    }

    // Created Matrix
    public String[][] createdMatrix() {
        String[][] matrix = new String[this.row][this.col];
        return matrix;
    }

    // Get number of items
    public int numberOfItems() {
        return this.row * this.col;
    }

    // Mines numbers
    public int numberOfMines() {
        int size = this.numberOfItems();
        return (int) Math.ceil((double) size / 4);
    }

    // Fill mines
    public void fillMines() {
        Random random = new Random();
        int mines = numberOfMines();
        for (int i = 0; i < mines; i++) {
            int row, col;
            do {
                row = random.nextInt(this.row);
                col = random.nextInt(this.col);
            } while (mineMatrix[row][col] != null);  // Ensure we don't place a mine where one already exists
            mineMatrix[row][col] = "*";
        }
    }

    // Initialize player matrix
    public void initializePlayerMatrix() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                playerMatrix[i][j] = "-";
            }
        }
    }

    // Method to print the mine matrix (for testing purposes)
    public void printMineMatrix() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (mineMatrix[i][j] == null) {
                    System.out.print("- ");
                } else {
                    System.out.print(mineMatrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    // Method to print the player matrix
    public void printPlayerMatrix() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                System.out.print(playerMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to reveal a cell
    public void revealCell(int r, int c) {
        if (r < 0 || r >= this.row || c < 0 || c >= this.col || revealed[r][c]) {
            return;
        }

        revealed[r][c] = true;

        if (mineMatrix[r][c] != null) {
            playerMatrix[r][c] = mineMatrix[r][c];
            return;
        }

        int mineCount = countMinesAround(r, c);
        playerMatrix[r][c] = Integer.toString(mineCount);

        if (mineCount == 0) {
            revealCell(r - 1, c - 1);
            revealCell(r - 1, c);
            revealCell(r - 1, c + 1);
            revealCell(r, c - 1);
            revealCell(r, c + 1);
            revealCell(r + 1, c - 1);
            revealCell(r + 1, c);
            revealCell(r + 1, c + 1);
        }
    }

    // Method to count mines around a cell
    public int countMinesAround(int r, int c) {
        int count = 0;
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && i < this.row && j >= 0 && j < this.col && mineMatrix[i][j] != null) {
                    count++;
                }
            }
        }
        return count;
    }

    // Check if all non-mine cells are revealed
    public boolean checkWin() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (mineMatrix[i][j] == null && !revealed[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Main game loop
    public void play() {
        Scanner inp = new Scanner(System.in);
        boolean isPlaying = true;

        while (isPlaying) {
            printPlayerMatrix();
            System.out.print("Enter row: ");
            int r = inp.nextInt();
            System.out.print("Enter column: ");
            int c = inp.nextInt();

            if (r < 0 || r >= this.row || c < 0 || c >= this.col) {
                System.out.println("Invalid coordinates. Please try again.");
                continue;
            }

            if (revealed[r][c]) {
                System.out.println("This cell is already revealed. Please try again.");
                continue;
            }

            if (mineMatrix[r][c] != null) {
                System.out.println("You hit a mine! Game over.");
                printMineMatrix();
                isPlaying = false;
            } else {
                revealCell(r, c);
                if (checkWin()) {
                    System.out.println("Congratulations! You have won the game.");
                    printPlayerMatrix();
                    isPlaying = false;
                }
            }
        }
    }

}