public class Main {
    public static void main(String[] args) {

        Board gameBoard = new Board();
        gameBoard.generateBoards();
        ChessGUI chessGUI = new ChessGUI(gameBoard);
        chessGUI.startChessGUI();
        System.out.println();

    }
}