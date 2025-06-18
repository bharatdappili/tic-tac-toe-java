import java.util.ArrayList;

public class TicTacToe {
    private static Board board = new Board();
    private static boolean gameEnded = false;
    private static boolean player = true; // true = player (Cross), false = computer (Circle)

    public static void main(String[] args) {
        System.out.println(board);
        while (!gameEnded) {
            Position position = null;
            if (player) {
                // Automatically pick the first available free position (simulate player move)
                ArrayList<Position> freePositions = board.getFreePositions();
                if (freePositions.isEmpty()) {
                    break; // no moves left
                }
                position = freePositions.get(0);
                board = new Board(board, position, PlayerSign.Cross);
                System.out.println("Player moves to: (" + position.getColumn() + "," + position.getRow() + ")");
            } else {
                board = findBestMove(board);
                System.out.println("Computer moves:");
            }
            player = !player;
            System.out.println(board);
            evaluateGame();
        }
    }

    private static Board findBestMove(Board board) {
        ArrayList<Position> positions = board.getFreePositions();
        Board bestChild = null;
        int previous = Integer.MIN_VALUE;
        for (Position p : positions) {
            Board child = new Board(board, p, PlayerSign.Circle);
            int current = min(child);
            if (current > previous) {
                bestChild = child;
                previous = current;
            }
        }
        return bestChild;
    }

    public static int max(Board board) {
        GameState gameState = board.getGameState();
        if (gameState == GameState.CircleWin)
            return 1;
        else if (gameState == GameState.CrossWin)
            return -1;
        else if (gameState == GameState.Draw)
            return 0;
        ArrayList<Position> positions = board.getFreePositions();
        int best = Integer.MIN_VALUE;
        for (Position p : positions) {
            Board b = new Board(board, p, PlayerSign.Circle);
            int move = min(b);
            if (move > best)
                best = move;
        }
        return best;
    }

    public static int min(Board board) {
        GameState gameState = board.getGameState();
        if (gameState == GameState.CircleWin)
            return 1;
        else if (gameState == GameState.CrossWin)
            return -1;
        else if (gameState == GameState.Draw)
            return 0;
        ArrayList<Position> positions = board.getFreePositions();
        int best = Integer.MAX_VALUE;
        for (Position p : positions) {
            Board b = new Board(board, p, PlayerSign.Cross);
            int move = max(b);
            if (move < best)
                best = move;
        }
        return best;
    }

    private static void evaluateGame() {
        GameState gameState = board.getGameState();
        gameEnded = true;
        switch (gameState) {
            case CrossWin:
                System.out.println("Player Won!");
                break;
            case CircleWin:
                System.out.println("Computer Won!");
                break;
            case Draw:
                System.out.println("Draw!");
                break;
            default:
                gameEnded = false;
                break;
        }
    }
}

final class Position {
    private final int column;
    private final int row;

    public Position(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
}

enum PlayerSign {
    Cross, Circle
}

enum GameState {
    Incomplete, CrossWin, CircleWin, Draw
}

class Board {
    private char[][] board; // e = empty, x = cross, o = circle.

    public Board() {
        board = new char[3][3];
        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                board[x][y] = 'e'; // Board initially empty
    }

    public Board(Board from, Position position, PlayerSign sign) {
        board = new char[3][3];
        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                board[x][y] = from.board[x][y];
        board[position.getColumn()][position.getRow()] = sign == PlayerSign.Cross ? 'x' : 'o';
    }

    public ArrayList<Position> getFreePositions() {
        ArrayList<Position> retArr = new ArrayList<>();
        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                if (board[x][y] == 'e')
                    retArr.add(new Position(x, y));
        return retArr;
    }

    public GameState getGameState() {
        if (hasWon('x'))
            return GameState.CrossWin;
        else if (hasWon('o'))
            return GameState.CircleWin;
        else if (getFreePositions().size() == 0)
            return GameState.Draw;
        else
            return GameState.Incomplete;
    }

    private boolean hasWon(char sign) {
        // Check diagonals
        if (board[0][0] == sign && board[1][1] == sign && board[2][2] == sign)
            return true;
        if (board[0][2] == sign && board[1][1] == sign && board[2][0] == sign)
            return true;

        // Check rows
        for (int y = 0; y < 3; y++) {
            if (board[0][y] == sign && board[1][y] == sign && board[2][y] == sign)
                return true;
        }

        // Check columns
        for (int x = 0; x < 3; x++) {
            if (board[x][0] == sign && board[x][1] == sign && board[x][2] == sign)
                return true;
        }

        return false;
    }

    public boolean isMarked(Position position) {
        return board[position.getColumn()][position.getRow()] != 'e';
    }

    public String toString() {
        StringBuilder retString = new StringBuilder("\n");
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (board[x][y] == 'x' || board[x][y] == 'o')
                    retString.append("[").append(board[x][y]).append("]");
                else
                    retString.append("[ ]");
            }
            retString.append("\n");
        }
        return retString.toString();
    }
}
