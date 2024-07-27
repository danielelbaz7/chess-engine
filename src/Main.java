public class Main {
    public static void main(String[] args) {

        //generates a board and a gui based on it
        Board gameBoard = new Board();
        ChessGUI chessGUI = new ChessGUI(gameBoard);
        System.out.println(System.currentTimeMillis());
        System.out.println(ArtificialIntelligence.countOfOps);
        System.out.println(ArtificialIntelligence.findBestMove(gameBoard, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, gameBoard.whiteTurn).getEval());
        System.out.println(ArtificialIntelligence.countOfOps);
        System.out.println(System.currentTimeMillis());
    }
}