public class Main {
    public static void main(String[] args) {

        //generates a board and a gui based on it
        Board gameBoard = new Board();
        ChessGUI chessGUI = new ChessGUI(gameBoard);

        System.out.println(ArtificialIntelligence.countOfOps);
        System.out.println(System.currentTimeMillis());
        for(int i = 0; i < 1; i++) {
            ArtificialIntelligence.findBestMove(gameBoard, 5, gameBoard.whiteTurn);
        }
        System.out.println(System.currentTimeMillis());
        System.out.println(ArtificialIntelligence.countOfOps);

    }
}