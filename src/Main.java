public class Main {
    public static void main(String[] args) {

        //generates a board and a gui based on it
        Board gameBoard = new Board();
        ChessGUI chessGUI = new ChessGUI(gameBoard);
        System.out.println(ArtificialIntelligence.findBestMove(gameBoard, 3, true, 1).getMove());

    }
}